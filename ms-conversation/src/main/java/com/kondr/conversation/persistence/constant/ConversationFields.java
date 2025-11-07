package com.kondr.conversation.persistence.constant;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы, представляющие имена полей в документе диалога ({@link ConversationDocument}).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConversationFields {

  public static final String PARTICIPANTS = "participants";
  public static final String LAST_MESSAGE_DATE = "lastMessageDate";
  public static final String LAST_MESSAGE_PREVIEW = "lastMessagePreview";
  public static final String LAST_MESSAGE_SEQ = "lastMessageSeq";

}