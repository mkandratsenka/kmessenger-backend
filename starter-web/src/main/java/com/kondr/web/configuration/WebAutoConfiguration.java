package com.kondr.web.configuration;

import com.kondr.web.interceptor.GlobalRestExceptionHandler;
import com.kondr.web.properties.CorsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Автоматическая конфигурация, настраивающая инфраструктуру для веб-приложения.
 */
@AutoConfiguration
public class WebAutoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "app.web.exception-handler.global", name = "enabled", havingValue = "true", matchIfMissing = true)
  @ConditionalOnMissingBean(GlobalRestExceptionHandler.class)
  public GlobalRestExceptionHandler globalRestExceptionHandler() {
    return new GlobalRestExceptionHandler();
  }

}