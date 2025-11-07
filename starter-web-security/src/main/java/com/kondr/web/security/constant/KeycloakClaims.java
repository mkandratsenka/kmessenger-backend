package com.kondr.web.security.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы, представляющие имена стандартных полей (Claims) в токене JWT,
 * выдаваемом сервером Keycloak.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeycloakClaims {

  public static final String ID = "sub";
  public static final String EMAIL = "email";

}