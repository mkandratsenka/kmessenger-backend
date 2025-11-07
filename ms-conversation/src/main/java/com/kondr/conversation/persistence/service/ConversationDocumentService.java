package com.kondr.conversation.persistence.service;

import com.kondr.conversation.dto.request.ConversationRequestDto;
import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.domain.MessageDocument;
import com.kondr.conversation.persistence.domain.model.ConversationCreationResult;
import reactor.core.publisher.Mono;

/**
 * Сервис для работы с документами диалогов.
 */
public interface ConversationDocumentService {

  /**
   * Создает и сохраняет новый документ диалога на основе DTO.
   *
   * @param conversationDto DTO с данными для нового диалога.
   * @return {@link Mono} с результатом создания диалога.
   */
  Mono<ConversationCreationResult> create(ConversationRequestDto conversationDto);

  /**
   * Обновляет документ диалога при получении нового сообщения.
   *
   * @param message документ нового сообщения.
   * @return {@link Mono} с обновленным документом диалога.
   */
  Mono<ConversationDocument> updateOnNewMessage(MessageDocument message);

}