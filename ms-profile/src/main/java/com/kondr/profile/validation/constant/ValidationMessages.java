package com.kondr.profile.validation.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Содержит текстовые сообщения, используемые при валидации.
 * <p>
 * Именование констант: FIELD_CONDITION (например, USERNAME_EMPTY).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationMessages {

  public static final String USERNAME_TAKEN = "Имя пользователя уже занято";
  public static final String USERNAME_EMPTY = "Не указано имя пользователя";

}