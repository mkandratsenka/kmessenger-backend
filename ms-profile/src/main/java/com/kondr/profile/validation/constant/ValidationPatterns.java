package com.kondr.profile.validation.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы, содержащие регулярные выражения (шаблоны) для валидации полей.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationPatterns {

  public static final String USERNAME = "^[a-zA-Z0-9]*$"; // латиница + цифры
  public static final String NAME = "^[a-zA-Zа-яА-ЯёЁ\\\\-]*$"; // латиница/кириллица и '-'

}