package com.kondr.conversation.persistence.domain;

import com.kondr.conversation.persistence.constant.ConversationCollectionNames;
import com.kondr.conversation.persistence.domain.embedded.InterlocutorPreview;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Документ, содержащий данные превью диалога.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(ConversationCollectionNames.CONVERSATION_PREVIEWS)
public class ConversationPreviewDocument {

  @Id
  private ObjectId id;

  /**
   * ID связанного диалога.
   */
  @Indexed
  private ObjectId conversationId;

  /**
   * ID пользователя, которому принадлежит превью.
   */
  @Indexed
  private String userId;

  /**
   * Содержит краткую информацию о собеседнике.
   */
  private InterlocutorPreview interlocutor;

  /**
   * Порядковый номер последнего прочитанного сообщения.
   */
  private Long readMessageSeq;

  /**
   * Дата и время последнего прочтения сообщения.
   */
  private Instant lastReadDate;

  /**
   * Дата создания документа-превью.
   */
  @CreatedDate
  private Instant createdAt;

}