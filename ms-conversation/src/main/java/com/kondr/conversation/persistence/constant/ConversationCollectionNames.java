package com.kondr.conversation.persistence.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы, представляющие имена коллекций в базе данных MongoDB.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConversationCollectionNames {

  public static final String CONVERSATIONS = "conversations";
  public static final String CONVERSATION_PREVIEWS = "conversation_previews";
  public static final String MESSAGES = "messages";

}