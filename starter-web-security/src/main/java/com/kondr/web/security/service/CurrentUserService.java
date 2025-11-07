package com.kondr.web.security.service;

import com.kondr.web.security.exception.UnauthorizedUserException;
import com.kondr.web.security.model.KeycloakUser;
import reactor.core.publisher.Mono;

/**
 * Сервис для получения информации о текущем аутентифицированном пользователе в реактивном
 * контексте.
 */
public interface CurrentUserService {

  /**
   * Возвращает токен текущего пользователя.
   *
   * @return токен.
   */
  Mono<String> getToken();

  /**
   * Возвращает модель текущего пользователя Keycloak.
   *
   * @return пользователь Keycloak.
   */
  Mono<KeycloakUser> getKeycloakUser();

  /**
   * Возвращает модель текущего пользователя Keycloak или ошибку.
   *
   * @return модель пользователя, или ошибку {@link UnauthorizedUserException}, если пользователь не
   * аутентифицирован.
   */
  Mono<KeycloakUser> getKeycloakUserOrError();

  /**
   * Возвращает ID текущего пользователя.
   *
   * @return ID пользователя.
   */
  Mono<String> getId();

  /**
   * Возвращает ID текущего пользователя или ошибку.
   *
   * @return ID пользователя, или ошибку {@link UnauthorizedUserException}, если пользователь не
   * аутентифицирован.
   */
  Mono<String> getIdOrError();

}