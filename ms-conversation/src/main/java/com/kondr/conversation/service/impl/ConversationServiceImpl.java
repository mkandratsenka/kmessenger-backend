package com.kondr.conversation.service.impl;

import com.kondr.conversation.dto.request.ConversationRequestDto;
import com.kondr.conversation.mapper.MessageEventMapper;
import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import com.kondr.conversation.messaging.redis.publisher.RedisMessagePublisher;
import com.kondr.conversation.persistence.domain.model.ConversationCreationResult;
import com.kondr.conversation.persistence.service.ConversationDocumentService;
import com.kondr.conversation.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

  private final ConversationDocumentService conversationDocumentService;

  private final MessageEventMapper messageEventMapper;

  private final RedisMessagePublisher redisMessagePublisher;

  @Override
  public Mono<ConversationCreationResult> createConversation(
      ConversationRequestDto conversationDto) {

    return conversationDocumentService.create(conversationDto)
        .doOnSuccess(conversationCreationResult -> {
              var conversation = conversationCreationResult.conversation();

              MessageCreatedEvent messageCreatedEvent = messageEventMapper
                  .toMessageCreatedEvent(conversationCreationResult.message(), conversation);

              Flux.fromIterable(conversation.getParticipants())
                  .flatMap(userId -> redisMessagePublisher.publish(messageCreatedEvent, userId))
                  .subscribe(); // паблишим на lettuce-epollEventLoop, но не ждем ответа
            }
        );
  }

}