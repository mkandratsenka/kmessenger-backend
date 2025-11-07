package com.kondr.conversation.service.impl;

import com.kondr.conversation.dto.request.MessageRequestDto;
import com.kondr.conversation.dto.response.NewMessageResponseDto;
import com.kondr.conversation.mapper.MessageEventMapper;
import com.kondr.conversation.mapper.MessageMapper;
import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import com.kondr.conversation.messaging.kafka.producer.KafkaMessageProducer;
import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import com.kondr.conversation.messaging.redis.publisher.RedisMessagePublisher;
import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.domain.MessageDocument;
import com.kondr.conversation.persistence.service.ConversationDocumentService;
import com.kondr.conversation.persistence.service.ConversationPreviewDocumentService;
import com.kondr.conversation.persistence.service.MessageDocumentService;
import com.kondr.conversation.service.MessageService;
import com.kondr.mongo.factory.ObjectIdFactory;
import com.kondr.mongo.mapper.ObjectIdMapper;
import com.kondr.mongo.utils.MongoExceptionUtils;
import com.kondr.web.security.service.CurrentUserService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final CurrentUserService currentUserService;

  private final ConversationDocumentService conversationDocumentService;

  private final ConversationPreviewDocumentService conversationPreviewDocumentService;

  private final MessageDocumentService messageDocumentService;

  private final TransactionalOperator transactionalOperator;

  private final ObjectIdFactory objectIdFactory;

  private final ObjectIdMapper objectIdMapper;

  private final MessageMapper messageMapper;

  private final MessageEventMapper messageEventMapper;

  private final KafkaMessageProducer kafkaMessageProducer;

  private final RedisMessagePublisher redisMessagePublisher;

  @Override
  public Mono<NewMessageResponseDto> sendMessage(String conversationId,
      MessageRequestDto messageDto) {

    return currentUserService.getIdOrError()
        .flatMap(userId -> {
              String tempId = objectIdFactory.newObjectIdHex();
              Instant sentAt = Instant.now();

              NewMessageEvent newMessageEvent =
                  getNewMessageEvent(tempId, conversationId, userId, sentAt, messageDto.content());

              return kafkaMessageProducer.send(newMessageEvent)
                  .thenReturn(getNewMessageResponseDto(tempId, sentAt));
            }
        );
  }

  @Override
  public Mono<Void> processNewMessageEvent(NewMessageEvent newMessageEvent) {
    MessageDocument newMessage = messageMapper.from(newMessageEvent);

    return conversationDocumentService.updateOnNewMessage(newMessage)
        .flatMap(conversation -> {
          Long seq = conversation.getLastMessageSeq();
          return Mono.when(
                  createMessage(newMessage, seq),
                  commitPreviewReadSeq(conversation, newMessage, seq)
              )
              .thenReturn(conversation);
        })
        .as(transactionalOperator::transactional)
        .retryWhen(Retry.backoff(3, Duration.ofMillis(50))
            .filter(MongoExceptionUtils::isWriteConflictException)
        )
        .flatMap(conversation -> {
          List<String> participants = conversation.getParticipants();
          if (CollectionUtils.isEmpty(participants)) {
            String conversationId = objectIdMapper.toString(conversation.getId());
            return Mono.error(
                new IllegalArgumentException("No participants in conversation " + conversationId));
          }

          MessageCreatedEvent messageCreatedEvent =
              messageEventMapper.toMessageCreatedEvent(newMessage, conversation);

          return publishToParticipants(messageCreatedEvent, participants);
        });
  }

  private NewMessageEvent getNewMessageEvent(String tempId, String conversationId, String senderId,
      Instant sentAt, String content) {

    return NewMessageEvent.builder()
        .tempId(tempId)
        .conversationId(conversationId)
        .senderId(senderId)
        .content(content)
        .sentAt(sentAt)
        .build();
  }

  private NewMessageResponseDto getNewMessageResponseDto(String tempId, Instant sentAt) {
    return NewMessageResponseDto.builder()
        .tempId(tempId)
        .sentAt(sentAt)
        .build();
  }

  private Mono<MessageDocument> createMessage(MessageDocument newMessage, Long seq) {
    newMessage.setSeq(seq);
    return messageDocumentService.create(newMessage);
  }

  private Mono<Instant> commitPreviewReadSeq(ConversationDocument conversation,
      MessageDocument message, Long seq) {

    return conversationPreviewDocumentService
        .commitReadSeq(conversation.getId(), message.getSenderId(), seq);
  }

  private Mono<Void> publishToParticipants(MessageCreatedEvent messageCreatedEvent,
      List<String> participants) {

    return Flux.fromIterable(participants)
        .flatMap(userId ->
            redisMessagePublisher.publish(messageCreatedEvent, userId)
                .onErrorResume(error -> {
                  log.warn("Failed to publish messageCreatedEvent.", error);
                  return Mono.empty();
                })
        )
        .then();
  }

}