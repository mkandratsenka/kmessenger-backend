package com.kondr.conversation.persistence.domain;

import com.kondr.conversation.persistence.constant.ConversationCollectionNames;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Документ, содержащий данные сообщения.
 */
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(ConversationCollectionNames.MESSAGES)
public class MessageDocument {

  @Id
  private ObjectId id;

  /**
   * ID диалога, к которому относится сообщение.
   */
  @Indexed
  private ObjectId conversationId;

  /**
   * ID отправителя сообщения.
   */
  private String senderId;

  /**
   * Содержимое сообщения.
   */
  private String content;

  /**
   * Порядковый номер сообщения в рамках диалога.
   */
  @Indexed
  private Long seq;

  /**
   * Дата и время отправки сообщения.
   */
  private Instant sentAt;

}