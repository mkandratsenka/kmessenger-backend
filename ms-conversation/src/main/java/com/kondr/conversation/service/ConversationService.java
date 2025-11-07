package com.kondr.conversation.service;

import com.kondr.conversation.dto.request.ConversationRequestDto;
import com.kondr.conversation.persistence.domain.model.ConversationCreationResult;
import reactor.core.publisher.Mono;

/**
 * Интеграционный сервис для работы с диалогами.
 */
public interface ConversationService {

  /**
   * Создает новый диалог на основе DTO.
   *
   * @param conversationDto DTO с данными для создания диалога.
   * @return {@link Mono} с результатом создания диалога.
   */
  Mono<ConversationCreationResult> createConversation(ConversationRequestDto conversationDto);

}