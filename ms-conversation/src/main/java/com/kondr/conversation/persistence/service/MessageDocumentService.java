package com.kondr.conversation.persistence.service;

import com.kondr.conversation.dto.request.MessageBatchRequestDto;
import com.kondr.conversation.persistence.domain.MessageDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Сервис для работы с документами сообщений.
 */
public interface MessageDocumentService {

  /**
   * Создает и сохраняет новый документ сообщения в базе данных.
   *
   * @param message документ сообщения для сохранения.
   * @return {@link Mono} с сохраненным документом сообщения.
   */
  Mono<MessageDocument> create(MessageDocument message);

  /**
   * Начитывает пачку сообщений для указанного диалога, используя пагинацию по порядковому номеру
   * ({@code seq}).
   *
   * @param conversationId      ID чата.
   * @param messageBatchRequest DTO с параметрами пагинации.
   * @return {@link Flux} с найденными документами сообщений.
   */
  Flux<MessageDocument> findMessages(String conversationId,
      MessageBatchRequestDto messageBatchRequest);

}