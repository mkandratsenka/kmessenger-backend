package com.kondr.conversation.persistence.factory;

import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.embedded.InterlocutorPreview;
import java.time.Instant;
import org.bson.types.ObjectId;

/**
 * Фабрика для создания документов превью диалогов ({@link ConversationPreviewDocument}).
 */
public interface ConversationPreviewDocumentFactory {

  /**
   * Создает новый объект документа превью диалога.
   *
   * @param conversationId      ID диалога.
   * @param userId              ID пользователя, для которого создается превью диалога.
   * @param interlocutorPreview информация о собеседнике.
   * @param lastReadDate        дата последнего прочитанного сообщения.
   * @param readMessageSeq      порядковый номер последнего прочитанного сообщения.
   * @return новый объект {@link ConversationPreviewDocument}.
   */
  ConversationPreviewDocument build(ObjectId conversationId, String userId,
      InterlocutorPreview interlocutorPreview, Instant lastReadDate, Long readMessageSeq);

}