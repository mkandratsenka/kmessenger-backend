package com.kondr.conversation.persistence.dao;

import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.projection.ConversationPreviewProjection;
import java.time.Instant;
import java.util.List;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * DAO для работы с документами превью диалогов в MongoDB.
 */
public interface ConversationPreviewDocumentDAO {

  /**
   * Проверяет существование превью диалога по его ID и ID пользователя.
   *
   * @param id     ID документа превью.
   * @param userId ID пользователя.
   * @return {@link Mono} с результатом проверки.
   */
  Mono<Boolean> existsByIdAndUserId(String id, String userId);

  /**
   * Проверяет существование превью диалога по ID диалога и ID пользователя.
   *
   * @param conversationId ID диалога.
   * @param userId         ID пользователя.
   * @return {@link Mono} с результатом проверки.
   */
  Mono<Boolean> existsByConversationIdAndUserId(String conversationId, String userId);

  /**
   * Сохраняет указанные документы превью диалогов в базе данных.
   *
   * @param previews список документов превью для сохранения.
   * @return {@link Flux} с сохраненными документами превью диалогов.
   */
  Flux<ConversationPreviewDocument> createAll(List<ConversationPreviewDocument> previews);

  /**
   * Находит и собирает обогащенные проекции превью диалогов для указанного пользователя.
   *
   * @param userId ID пользователя.
   * @return {@link Flux} с обогащенными проекциями превью диалогов.
   */
  Flux<ConversationPreviewProjection> findPreviewProjectionsByUserId(String userId);

  /**
   * Находит превью диалога по ID диалога и ID пользователя.
   *
   * @param conversationId ID диалога.
   * @param userId         ID пользователя.
   * @return {@link Mono} с найденным документом превью.
   */
  Mono<ConversationPreviewDocument> findFirstPreview(String conversationId, String userId);

  /**
   * Фиксирует порядковый номер прочитанного сообщения по ID превью.
   *
   * @param id      ID документа превью.
   * @param readSeq порядковый номер последнего прочитанного сообщения.
   * @return {@link Mono} с датой и временем в случае успеха обновления счетчика.
   */
  Mono<Instant> commitReadSeq(String id, Long readSeq);

  /**
   * Фиксирует порядковый номер прочитанного сообщения по ID диалога и ID пользователя.
   *
   * @param conversationId ID диалога.
   * @param userId         ID пользователя.
   * @param readSeq        порядковый номер последнего прочитанного сообщения.
   * @return {@link Mono} с датой и временем в случае успеха обновления счетчика.
   */
  Mono<Instant> commitReadSeq(ObjectId conversationId, String userId, Long readSeq);

}