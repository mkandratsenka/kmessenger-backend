package com.kondr.web.security.configuration;

import com.kondr.keycloak.configuration.KeycloakClientAutoConfiguration;
import com.kondr.keycloak.properties.KeycloakClientProperties;
import com.kondr.web.core.factory.ClientHttpConnectorFactory;
import com.kondr.web.security.exception.UnauthorizedUserException;
import com.kondr.web.security.service.CurrentUserService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Автоматическая конфигурация, предоставляющая инфраструктуру для построения {@link WebClient},
 * использующих токены для доступа к защищенным ресурсам.
 */
@AutoConfiguration(after = KeycloakClientAutoConfiguration.class)
@RequiredArgsConstructor
public class WebClientSecurityAutoConfiguration {

  /**
   * Имя бина для {@link WebClient.Builder}, необходимого для построения {@link WebClient}, которые
   * используются для сервисных (M2M) вызовов с использованием токена самого сервиса.
   */
  public static final String SERVICE_WEB_CLIENT_BUILDER_NAME = "serviceWebClientBuilder";

  /**
   * Имя бина для {@link WebClient.Builder}, необходимого для построения {@link WebClient},
   * использующих токен текущего аутентифицированного пользователя.
   */
  public static final String USER_WEB_CLIENT_BUILDER_NAME = "userWebClientBuilder";

  private static final String SERVICE_POOL_NAME = "service-pool";

  @Value("${global.connection.tcp.max-idle-time}")
  private Duration tcpMaxIdleTime;

  private final CurrentUserService currentUserService;

  private final ClientHttpConnectorFactory clientHttpConnectorFactory;

  @ConditionalOnBean({
      ReactiveOAuth2AuthorizedClientManager.class,
      KeycloakClientProperties.class
  })
  @ConditionalOnMissingBean(name = SERVICE_WEB_CLIENT_BUILDER_NAME)
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  @Bean(SERVICE_WEB_CLIENT_BUILDER_NAME)
  public WebClient.Builder serviceWebClientBuilder(
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
      KeycloakClientProperties keycloakClientProperties) {

    WebClient.Builder builder = createWebClientBuilder();

    var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    oauth.setDefaultClientRegistrationId(keycloakClientProperties.getRegistrationId());

    builder.filter(oauth);

    return builder;
  }

  @ConditionalOnMissingBean(name = USER_WEB_CLIENT_BUILDER_NAME)
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  @Bean(USER_WEB_CLIENT_BUILDER_NAME)
  public WebClient.Builder userWebClientBuilder() {
    WebClient.Builder builder = createWebClientBuilder();

    ExchangeFilterFunction userTokenFilter = (request, next) -> currentUserService.getToken()
        .flatMap(token -> next.exchange(addBearerAuth(request, token)))
        .switchIfEmpty(Mono.defer(() -> Mono.error(new UnauthorizedUserException())));

    builder.filter(userTokenFilter);

    return builder;
  }

  private WebClient.Builder createWebClientBuilder() {
    WebClient.Builder builder = WebClient.builder()
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

    builder.clientConnector(clientHttpConnectorFactory.build(SERVICE_POOL_NAME, tcpMaxIdleTime));

    return builder;
  }

  private ClientRequest addBearerAuth(ClientRequest request, String token) {
    return ClientRequest.from(request).headers(h -> h.setBearerAuth(token)).build();
  }

}