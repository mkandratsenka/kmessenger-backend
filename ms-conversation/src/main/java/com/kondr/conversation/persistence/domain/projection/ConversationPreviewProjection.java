package com.kondr.conversation.persistence.domain.projection;

import com.kondr.conversation.persistence.domain.embedded.InterlocutorPreview;
import java.time.Instant;
import lombok.Builder;

/**
 * Проекция, используемая для извлечения связанных данных, необходимых для отображения обогащенного
 * списка превью диалогов пользователя.
 * <p>
 * Комбинирует поля документа превью диалога и данные из документа связанного диалога.
 *
 * @param conversationId     ID диалога.
 * @param lastMessagePreview сокращенное содержимое последнего отправленного сообщения.
 * @param lastMessageDate    дата последнего отправленного сообщения.
 * @param lastMessageSeq     порядковый номер последнего отправленного сообщения.
 * @param previewId          ID документа превью.
 * @param interlocutor       встроенный документ с данными собеседника.
 * @param readMessageSeq     порядковый номер последнего прочитанного сообщения.
 */
@Builder
public record ConversationPreviewProjection(

    String conversationId,
    String lastMessagePreview,
    Instant lastMessageDate,
    Long lastMessageSeq,

    String previewId,
    InterlocutorPreview interlocutor,
    Long readMessageSeq) {

  public static final String CONVERSATION_ID = "conversationId";
  public static final String LAST_MESSAGE_PREVIEW = "lastMessagePreview";
  public static final String LAST_MESSAGE_DATE = "lastMessageDate";
  public static final String LAST_MESSAGE_SEQ = "lastMessageSeq";

  public static final String PREVIEW_ID = "previewId";
  public static final String INTERLOCUTOR = "interlocutor";
  public static final String READ_MESSAGE_SEQ = "readMessageSeq";

}