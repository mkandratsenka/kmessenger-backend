package com.kondr.keycloak.configuration;

import com.kondr.keycloak.properties.KeycloakClientProperties;
import com.kondr.web.core.factory.WebClientFactory;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * Автоматическая конфигурация для настройки M2M (client credentials) аутентификации Keycloak.
 */
@Slf4j
@ConditionalOnProperty(prefix = "app.keycloak.client", name = "registration-id")
@AutoConfiguration(after = ReactiveOAuth2ClientAutoConfiguration.class)
@EnableConfigurationProperties(KeycloakClientProperties.class)
@RequiredArgsConstructor
public class KeycloakClientAutoConfiguration {

  private static final String KEYCLOAK_CLIENT_POOL_NAME = "keycloak-client-pool";

  @Value("${global.connection.tcp.max-idle-time}")
  private Duration tcpMaxIdleTime;

  private final WebClientFactory webClientFactory;

  @ConditionalOnBean({
      ReactiveClientRegistrationRepository.class,
      ReactiveOAuth2AuthorizedClientService.class
  })
  @Bean
  @ConditionalOnMissingBean
  public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
      ReactiveClientRegistrationRepository clientRegistrationRepository,
      ReactiveOAuth2AuthorizedClientService authorizedClientService) {

    log.info("Auto-configuring default ReactiveOAuth2AuthorizedClientManager.");

    var manager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientService);

    var tokenResponseClient = new WebClientReactiveClientCredentialsTokenResponseClient();
    tokenResponseClient.setWebClient(
        webClientFactory.build(KEYCLOAK_CLIENT_POOL_NAME, tcpMaxIdleTime));

    var provider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
        .clientCredentials(config -> config.accessTokenResponseClient(tokenResponseClient))
        .build();

    manager.setAuthorizedClientProvider(provider);

    return manager;
  }

}