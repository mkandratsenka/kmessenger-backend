package com.kondr.profile.persistence.domain;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Документ, содержащий данные профиля пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("profiles")
public class ProfileDocument {

  /**
   * ID пользователя. Совпадает с идентификатором (sub) в Keycloak.
   */
  @Id
  private String id;

  /**
   * Адрес электронной почты.
   */
  @Indexed(unique = true)
  private String email;

  /**
   * Имя пользователя в системе (никнейм).
   */
  private String username;

  /**
   * Фамилия пользователя.
   */
  private String lastName;

  /**
   * Личное имя пользователя.
   */
  private String firstName;

  /**
   * Дата создания пользователя в системе.
   * <p>
   * Аннотация {@code @CreatedDate} не работает, так как ID ({@code id}) устанавливается до первого
   * сохранения документа.
   */
  private Instant createdAt;

  /**
   * Дата последнего изменения данных пользователя.
   */
  @LastModifiedDate
  private Instant updatedAt;

  /**
   * Имя пользователя в нижнем регистре для поиска.
   * <p>
   * Пример: "user25"
   */
  @Indexed(unique = true)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private String usernameLower;

  /**
   * Объединение фамилии и имени в нижнем регистре для поиска.
   * <p>
   * Пример: "смирнов александр"
   */
  @Indexed
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private String fullNameLower;

}