package com.kondr.shared.utils;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Класс, содержащий набор утилитных методов для работы со строками.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppStringUtils {

  public static final String EMPTY = "";

  /**
   * Проверяет, является ли коллекция null, пустой или содержит только пустые (пробельные) строки.
   * <p>
   * Пример: isAllBlank([" ", null, ""]) → true
   */
  public static boolean isAllBlank(Collection<String> strings) {
    return strings == null || strings.stream().noneMatch(StringUtils::isNotBlank);
  }

}