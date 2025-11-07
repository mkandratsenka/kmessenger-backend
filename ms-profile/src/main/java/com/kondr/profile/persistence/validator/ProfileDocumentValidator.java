package com.kondr.profile.persistence.validator;

import com.kondr.profile.persistence.domain.ProfileDocument;
import reactor.core.publisher.Mono;

/**
 * Валидатор для проверки документов профилей.
 */
public interface ProfileDocumentValidator {

  /**
   * Выполняет валидацию документа профиля.
   *
   * @param profile документ профиля.
   * @return {@link Mono} с проверенным документом профиля или ошибкой валидации.
   */
  Mono<ProfileDocument> validate(ProfileDocument profile);

}