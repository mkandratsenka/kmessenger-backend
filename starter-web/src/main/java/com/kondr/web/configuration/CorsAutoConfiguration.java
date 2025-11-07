package com.kondr.web.configuration;

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
 * Опциональная автоматическая конфигурация CORS.
 * <p>
 * Spring Cloud Gateway агрессивно обрабатывает pre-flight (HTTP {@code OPTIONS}) запросы, не
 * позволяя делегировать их обработку целевым микросервисам.
 * <p>
 * При использовании Gateway рекомендуется отключать эту конфигурацию, поскольку CORS-заголовки уже
 * формируются на уровне Gateway. Одновременное включение CORS в Gateway и микросервисе приведёт к
 * дублированию заголовков ответов и ошибке в браузере.
 * <p>
 * Подробнее: <a
 * href="https://github.com/spring-cloud/spring-cloud-gateway/issues/830">spring-cloud-gateway#830</a>
 */
@ConditionalOnProperty(name = "cors.enabled", havingValue = "true")
@AutoConfiguration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public CorsWebFilter corsWebFilter(CorsConfigurationSource corsConfigurationSource) {
    return new CorsWebFilter(corsConfigurationSource);
  }

  @Bean
  @ConditionalOnMissingBean
  public CorsConfigurationSource corsConfigurationSource(CorsProperties properties) {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(properties.isAllowCredentials());
    config.setAllowedOrigins(properties.getAllowedOrigins());
    config.setAllowedMethods(properties.getAllowedMethods());
    config.setAllowedHeaders(properties.getAllowedHeaders());

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

}