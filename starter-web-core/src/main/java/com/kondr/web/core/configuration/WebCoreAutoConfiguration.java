package com.kondr.web.core.configuration;

import com.kondr.starter.factory.YamlPropertySourceFactory;
import com.kondr.web.core.factory.ClientHttpConnectorFactory;
import com.kondr.web.core.factory.WebClientFactory;
import com.kondr.web.core.factory.impl.ClientHttpConnectorFactoryImpl;
import com.kondr.web.core.factory.impl.WebClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * Автоматическая конфигурация, расширяющая стандартную инфраструктуру {@code WebFlux}.
 */
@AutoConfiguration
@PropertySource(value = "classpath:connection-settings.yml", factory = YamlPropertySourceFactory.class)
public class WebCoreAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ClientHttpConnectorFactory clientHttpConnectorFactory() {
    return new ClientHttpConnectorFactoryImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public WebClientFactory webClientFactory(ClientHttpConnectorFactory clientHttpConnectorFactory) {
    return new WebClientFactoryImpl(clientHttpConnectorFactory);
  }

}