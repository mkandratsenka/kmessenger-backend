package com.kondr.conversation.controller.rsocket;

import com.kondr.conversation.dto.request.ConversationRequestDto;
import com.kondr.conversation.dto.response.ConversationCreatedResponseDto;
import com.kondr.conversation.mapper.ConversationMapper;
import com.kondr.conversation.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class ConversationRSocketController {

  private final ConversationService conversationService;

  private final ConversationMapper conversationMapper;

  @MessageMapping("conversations.create")
  public Mono<ConversationCreatedResponseDto> createConversation(
      @RequestBody @Valid ConversationRequestDto conversationDto) {

    return conversationService.createConversation(conversationDto)
        .map(conversationMapper::toDto);
  }

}