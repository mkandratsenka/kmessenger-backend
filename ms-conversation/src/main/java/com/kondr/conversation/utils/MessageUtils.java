package com.kondr.conversation.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Класс, содержащий набор утилитных методов для работы с сообщениями.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtils {

  public static final int PREVIEW_MAX_LENGTH = 21;

  public static String getMessagePreview(String message) {
    return StringUtils.substring(message, 0, PREVIEW_MAX_LENGTH);
  }

}