package com.kondr.profile.persistence.service;

import com.kondr.profile.dto.ProfileRequestDto;
import com.kondr.profile.persistence.domain.ProfileDocument;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Сервис для работы с документами профилей.
 */
public interface ProfileDocumentService {

  /**
   * Создает новый профиль на основе DTO.
   *
   * @param profileDto DTO с данными профиля.
   * @return {@link Mono} с созданным профилем.
   */
  Mono<ProfileDocument> create(ProfileRequestDto profileDto);

  /**
   * Находит профиль текущего пользователя.
   *
   * @return {@link Mono} с профилем текущего пользователя.
   */
  Mono<ProfileDocument> findCurrentProfile();

  /**
   * Находит профили по списку ID.
   *
   * @param ids список ID профилей.
   * @return {@link Flux} с найденными профилями.
   */
  Flux<ProfileDocument> findByIds(List<String> ids);

  /**
   * Выполняет поиск профилей по заданным параметрам.
   *
   * @param username   никнейм (часть) пользователя.
   * @param nameTokens список токенов имени/фамилии.
   * @return {@link Flux} с найденными профилями.
   */
  Flux<ProfileDocument> findProfiles(String username, List<String> nameTokens);

}