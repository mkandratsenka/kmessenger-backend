package com.kondr.conversation.persistence.constant;

import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы, представляющие имена полей в документе превью диалога
 * ({@link ConversationPreviewDocument}).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConversationPreviewFields {

  public static final String USER_ID = "userId";
  public static final String CONVERSATION_ID = "conversationId";
  public static final String INTERLOCUTOR = "interlocutor";
  public static final String READ_MESSAGE_SEQ = "readMessageSeq";
  public static final String LAST_READ_DATE = "lastReadDate";

}