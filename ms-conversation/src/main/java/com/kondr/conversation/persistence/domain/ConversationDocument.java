package com.kondr.conversation.persistence.domain;

import com.kondr.conversation.persistence.constant.ConversationCollectionNames;
import java.time.Instant;
import java.util.List;
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
 * Документ, содержащий данные диалога.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(ConversationCollectionNames.CONVERSATIONS)
public class ConversationDocument {

  @Id
  private ObjectId id;

  /**
   * Ключ, используемый для обеспечения уникальности диалога между двумя участниками.
   */
  @Indexed(unique = true, sparse = true)
  private String uniqueKey;

  /**
   * Список ID участников диалога.
   */
  @Indexed
  private List<String> participants;

  /**
   * Дата и время последнего отправленного сообщения.
   */
  private Instant lastMessageDate;

  /**
   * Порядковый номер последнего отправленного сообщения в диалоге.
   */
  private Long lastMessageSeq;

  /**
   * Сокращенное содержимое последнего отправленного сообщения.
   */
  private String lastMessagePreview;

  /**
   * Дата создания диалога.
   */
  @CreatedDate
  private Instant createdAt;

}