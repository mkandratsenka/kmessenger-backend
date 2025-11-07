package com.kondr.conversation.persistence.dao;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import java.time.Instant;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

/**
 * DAO для работы с документами диалогов в MongoDB.
 */
public interface ConversationDocumentDAO {

  /**
   * Создает и сохраняет новый документ диалога в базе данных.
   *
   * @param conversation документ диалога для сохранения.
   * @return {@link Mono} с сохраненным документом диалога.
   */
  Mono<ConversationDocument> create(ConversationDocument conversation);

  /**
   * Обновляет документ диалога при получении нового сообщения.
   *
   * @param id                ID диалога.
   * @param newMessageDate    дата и время отправки нового сообщения.
   * @param newMessagePreview сокращенное содержимое нового сообщения.
   * @return {@link Mono} с обновленным документом диалога.
   */
  Mono<ConversationDocument> updateOnNewMessage(ObjectId id, Instant newMessageDate,
      String newMessagePreview);

}