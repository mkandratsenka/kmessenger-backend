package com.kondr.profile.persistence.dao;

import com.kondr.profile.persistence.domain.ProfileDocument;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * DAO для работы с документами профилей в MongoDB.
 */
public interface ProfileDocumentDAO {

  /**
   * Проверяет существование профиля по имени пользователя.
   *
   * @param username имя (никнейм) пользователя.
   * @return {@link Mono} с {@code true}, если профиль существует.
   */
  Mono<Boolean> existsByUsername(String username);

  /**
   * Ищет профиль по ID.
   *
   * @param id ID профиля.
   * @return {@link Mono} с найденным профилем или {@link Mono#empty()}, если не найден.
   */
  Mono<ProfileDocument> findById(String id);

  /**
   * Ищет профили по списку ID.
   *
   * @param ids список ID.
   * @return {@link Flux} с найденными профилями.
   */
  Flux<ProfileDocument> findByIds(List<String> ids);

  /**
   * Создаёт новый документ профиля.
   *
   * @param profile документ профиля.
   * @return {@link Mono} с сохранённым профилем.
   */
  Mono<ProfileDocument> create(ProfileDocument profile);

  /**
   * Выполняет поиск профилей по фильтрам.
   *
   * @param username      никнейм (часть) пользователя.
   * @param nameTokens    список токенов (частей) имени/фамилии для поиска.
   * @param ignoredUserId ID пользователя, который можно исключить из выборки.
   * @param limit         ограничение на количество результатов.
   * @return {@link Flux} с найденными профилями.
   */
  Flux<ProfileDocument> findProfiles(String username, List<String> nameTokens,
      String ignoredUserId, int limit);

}