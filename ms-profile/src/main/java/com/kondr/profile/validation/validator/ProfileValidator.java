package com.kondr.profile.validation.validator;

import com.kondr.profile.dto.ProfileRequestDto;
import com.kondr.web.validation.model.ValidationError;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Валидатор для данных профилей.
 */
public interface ProfileValidator {

  /**
   * Выполняет валидацию DTO для создания профиля.
   *
   * @param profileDto DTO с данными профиля.
   * @return {@link Mono} со списком выявленных ошибок валидации.
   */
  Mono<List<ValidationError>> validate(ProfileRequestDto profileDto);

}