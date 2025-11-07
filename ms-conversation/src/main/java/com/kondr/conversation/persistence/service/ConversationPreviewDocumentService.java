package com.kondr.conversation.persistence.service;

import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.projection.ConversationPreviewProjection;
import java.time.Instant;
import java.util.List;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Сервис для работы с документами превью диалогов.
 */
public interface ConversationPreviewDocumentService {

  /**
   * Сохраняет указанные документы превью диалогов.
   *
   * @param previews список документов превью для сохранения.
   * @return {@link Flux} с сохраненными документами превью диалогов.
   */
  Flux<ConversationPreviewDocument> createAll(List<ConversationPreviewDocument> previews);

  /**
   * Находит и собирает данные для превью диалогов текущего пользователя.
   *
   * @return {@link Flux} с проекциями собранных данных превью диалогов.
   */
  Flux<ConversationPreviewProjection> findCurrentUserConversationPreviews();

  /**
   * Находит превью диалога для текущего пользователя по ID диалога.
   *
   * @param conversationId ID диалога.
   * @return {@link Mono} с документом превью диалога.
   */
  Mono<ConversationPreviewDocument> findCurrentUserConversationPreview(String conversationId);

  /**
   * Фиксирует порядковый номер (sequence number) последнего прочитанного сообщения для указанного
   * превью диалога пользователя.
   *
   * @param id      ID превью диалога.
   * @param readSeq порядковый номер последнего прочитанного сообщения.
   * @return {@link Mono} с датой и временем в случае успеха обновления счетчика.
   */
  Mono<Instant> commitReadSeq(String id, Long readSeq);

  /**
   * Фиксирует порядковый номер (sequence number) последнего прочитанного сообщения по указанному
   * диалогу для указанного пользователя.
   *
   * @param conversationId ID диалога.
   * @param userId         ID пользователя.
   * @param readSeq        порядковый номер последнего прочитанного сообщения.
   * @return {@link Mono} с датой и временем в случае успеха обновления счетчика.
   */
  Mono<Instant> commitReadSeq(ObjectId conversationId, String userId, Long readSeq);

}