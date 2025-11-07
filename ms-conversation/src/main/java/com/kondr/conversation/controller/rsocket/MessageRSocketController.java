package com.kondr.conversation.controller.rsocket;

import com.kondr.conversation.dto.request.MessageBatchRequestDto;
import com.kondr.conversation.dto.request.MessageRequestDto;
import com.kondr.conversation.dto.response.MessageResponseDto;
import com.kondr.conversation.dto.response.NewMessageResponseDto;
import com.kondr.conversation.mapper.MessageMapper;
import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import com.kondr.conversation.messaging.redis.provider.RedisSubscriptionProvider;
import com.kondr.conversation.persistence.service.MessageDocumentService;
import com.kondr.conversation.service.MessageService;
import com.kondr.web.security.service.CurrentUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class MessageRSocketController {

  private final CurrentUserService currentUserService;

  private final MessageService messageService;

  private final MessageDocumentService messageDocumentService;

  private final MessageMapper messageMapper;

  private final RedisSubscriptionProvider redisSubscriptionProvider;

  @MessageMapping("me.messages.events")
  public Flux<MessageCreatedEvent> subscribeToMessageEvents() {
    return currentUserService.getIdOrError()
        .flatMapMany(redisSubscriptionProvider::subscribeToMessageEvents)
        .doOnSubscribe(s -> log.info("Subscription to messages.events started"))
        .doOnNext(msg -> log.info("Emitting message: {}", msg.messageId()))
        .doOnCancel(() -> log.info("Subscription to messages.events cancelled"))
        .doOnError(error -> log.error("Error in messages.events stream: {}", error.getMessage()));
  }

  @PreAuthorize("@conversationAccessCheckerImpl.hasAccess(#conversationId, #requester)")
  @MessageMapping("conversations.{conversationId}.messages")
  public Mono<List<MessageResponseDto>> getMessages(
      @DestinationVariable String conversationId,
      @Payload MessageBatchRequestDto messageBatchRequest,
      RSocketRequester requester) {

    return messageDocumentService.findMessages(conversationId, messageBatchRequest)
        .map(messageMapper::toDto)
        .collectList();
  }

  @PreAuthorize("@conversationAccessCheckerImpl.hasAccess(#conversationId, #requester)")
  @MessageMapping("conversations.{conversationId}.messages.send")
  public Mono<NewMessageResponseDto> sendMessage(
      @DestinationVariable String conversationId,
      @Valid MessageRequestDto messageDto,
      RSocketRequester requester) {

    return messageService.sendMessage(conversationId, messageDto);
  }

}