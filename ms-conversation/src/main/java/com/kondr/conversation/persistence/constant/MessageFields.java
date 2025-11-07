package com.kondr.conversation.persistence.constant;

import com.kondr.conversation.persistence.domain.MessageDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы, представляющие имена полей в документе сообщения ({@link MessageDocument}).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageFields {

  public static final String CONVERSATION_ID = "conversationId";
  public static final String SEQ = "seq";

}
