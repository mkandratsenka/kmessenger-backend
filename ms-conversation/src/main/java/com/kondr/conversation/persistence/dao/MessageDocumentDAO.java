package com.kondr.conversation.persistence.dao;

import com.kondr.conversation.persistence.domain.MessageDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * DAO для работы с документами сообщений в MongoDB.
 */
public interface MessageDocumentDAO {

  /**
   * Создает и сохраняет новый документ сообщения в базе данных.
   *
   * @param message документ сообщения для сохранения.
   * @return {@link Mono} с сохраненным документом сообщения.
   */
  Mono<MessageDocument> create(MessageDocument message);

  /**
   * Находит пачку сообщений, порядковый номер которых меньше, чем указанный {@code seq}.
   *
   * @param conversationId ID диалога.
   * @param seq            порядковый номер (исключительно), до которого нужно искать сообщения.
   * @param limit          максимальное количество сообщений для возврата.
   * @return {@link Flux} с найденными документами сообщений.
   */
  Flux<MessageDocument> findMessagesBeforeSeq(String conversationId, Long seq, int limit);

  /**
   * Находит пачку сообщений, порядковый номер которых больше, чем указанный {@code seq}.
   *
   * @param conversationId ID диалога.
   * @param seq            порядковый номер (исключительно), после которого нужно искать сообщения.
   * @param limit          максимальное количество сообщений для возврата.
   * @return {@link Flux} с найденными документами сообщений.
   */
  Flux<MessageDocument> findMessagesAfterSeq(String conversationId, Long seq, int limit);

}