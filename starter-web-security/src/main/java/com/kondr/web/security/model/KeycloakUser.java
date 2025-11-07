package com.kondr.web.security.model;

import lombok.Builder;

/**
 * Модель, представляющая основные данные аутентифицированного пользователя, извлеченные из
 * JWT-токена Keycloak.
 *
 * @param id    ID пользователя.
 * @param email адрес электронной почты пользователя.
 */
@Builder
public record KeycloakUser(String id, String email) {

}