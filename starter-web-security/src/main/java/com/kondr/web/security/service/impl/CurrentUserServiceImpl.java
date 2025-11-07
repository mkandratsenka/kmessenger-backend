package com.kondr.web.security.service.impl;

import com.kondr.web.security.constant.KeycloakClaims;
import com.kondr.web.security.exception.UnauthorizedUserException;
import com.kondr.web.security.model.KeycloakUser;
import com.kondr.web.security.service.CurrentUserService;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

public class CurrentUserServiceImpl implements CurrentUserService {

  @Override
  public Mono<String> getToken() {
    return getJwt()
        .map(Jwt::getTokenValue);
  }

  @Override
  public Mono<KeycloakUser> getKeycloakUser() {
    return getJwt()
        .map(jwt ->
            KeycloakUser.builder()
                .id(jwt.getClaim(KeycloakClaims.ID))
                .email(jwt.getClaim(KeycloakClaims.EMAIL))
                .build()
        );
  }

  @Override
  public Mono<KeycloakUser> getKeycloakUserOrError() {
    return getKeycloakUser()
        .switchIfEmpty(Mono.defer(() -> Mono.error(new UnauthorizedUserException())));
  }

  @Override
  public Mono<String> getId() {
    return getJwt()
        .map(jwt -> jwt.getClaim(KeycloakClaims.ID));
  }

  @Override
  public Mono<String> getIdOrError() {
    return getId()
        .switchIfEmpty(Mono.defer(() -> Mono.error(new UnauthorizedUserException())));
  }

  private Mono<Jwt> getJwt() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .cast(JwtAuthenticationToken.class)
        .map(JwtAuthenticationToken::getToken);
  }

}