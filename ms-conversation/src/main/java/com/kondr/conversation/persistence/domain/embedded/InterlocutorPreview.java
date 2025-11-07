package com.kondr.conversation.persistence.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Встроенный документ, содержащий краткую информацию о собеседнике для отображения в превью
 * диалога.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterlocutorPreview {

  /**
   * ID собеседника.
   */
  private String id;

  /**
   * Имя пользователя (никнейм) собеседника.
   */
  private String username;

  /**
   * Фамилия собеседника.
   */
  private String lastName;

  /**
   * Имя собеседника.
   */
  private String firstName;

}
