package com.kondr.keycloak.configuration;

import com.kondr.keycloak.properties.KeycloakResourceServerProperties;
import com.kondr.web.core.factory.WebClientFactory;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;

/**
 * Автоматическая конфигурация для настройки инфраструктуры, необходимой для парсинга и валидации
 * токенов Keycloak.
 */
@Slf4j
@ConditionalOnProperty(prefix = "app.keycloak.resource-server", name = "jwk-set-uri")
@AutoConfiguration
@EnableConfigurationProperties(KeycloakResourceServerProperties.class)
@RequiredArgsConstructor
public class KeycloakResourceServerAutoConfiguration {

  private static final String KEYCLOAK_RESOURCE_SERVER_POOL_NAME = "keycloak-resource-server-pool";

  @Value("${global.connection.tcp.max-idle-time}")
  private Duration tcpMaxIdleTime;

  private final WebClientFactory webClientFactory;

  /**
   * Заменяем дефолтный {@code WebClient} для Keycloak, чтобы избежать сетевых ошибок вида "Could
   * not obtain the keys" из-за "Connection reset by peer".
   * <p>
   * Подробнее: <a href="https://github.com/moby/moby/issues/31208">Docker Swarm overlay network
   * issue</a>
   */
  @Bean
  @ConditionalOnMissingBean
  public ReactiveJwtDecoder reactiveJwtDecoder(KeycloakResourceServerProperties props) {
    log.info("Auto-configuring default ReactiveJwtDecoder.");

    var nimbusDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(props.getJwkSetUri())
        .webClient(webClientFactory.build(KEYCLOAK_RESOURCE_SERVER_POOL_NAME, tcpMaxIdleTime))
        .build();

    var issuerValidator = JwtValidators.createDefaultWithIssuer(props.getIssuerUri());
    nimbusDecoder.setJwtValidator(issuerValidator);

    return nimbusDecoder;
  }

  @Bean
  @ConditionalOnMissingBean
  public ReactiveAuthenticationManager jwtAuthenticationManager(ReactiveJwtDecoder jwtDecoder) {
    return new JwtReactiveAuthenticationManager(jwtDecoder);
  }

}