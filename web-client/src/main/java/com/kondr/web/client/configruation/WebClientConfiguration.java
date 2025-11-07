package com.kondr.web.client.configruation;

import static com.kondr.web.security.configuration.WebClientSecurityAutoConfiguration.SERVICE_WEB_CLIENT_BUILDER_NAME;

import com.kondr.web.client.clients.ProfileClient;
import com.kondr.web.client.clients.impl.ProfileClientImpl;
import com.kondr.web.client.constant.ClientNames;
import com.kondr.web.security.configuration.WebClientSecurityAutoConfiguration;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient.Builder;

/**
 * Конфигурация, отвечающая за создание и настройку реактивных REST-клиентов для взаимодействия с
 * внешними сервисами.
 */
@Configuration
@AutoConfigureAfter(WebClientSecurityAutoConfiguration.class)
public class WebClientConfiguration {

  @Value("${app.rest.client.profile.base-url}")
  private String profileBaseUrl;

  /**
   * Создает {@link ProfileClient}, предназначенный для межсервисного (Service-to-Service)
   * взаимодействия, использующий токен текущего сервиса (M2M).
   */
  @ConditionalOnBean(name = SERVICE_WEB_CLIENT_BUILDER_NAME)
  @Bean(ClientNames.PROFILE_SERVICE_CLIENT)
  public ProfileClient profileServiceClient(
      @Qualifier(SERVICE_WEB_CLIENT_BUILDER_NAME) ObjectFactory<Builder> serviceWebClientBuilderFactory) {

    var serviceWebClientBuilder = serviceWebClientBuilderFactory.getObject();
    return new ProfileClientImpl(serviceWebClientBuilder.baseUrl(profileBaseUrl).build());
  }

}