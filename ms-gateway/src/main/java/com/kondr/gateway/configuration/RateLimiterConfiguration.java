package com.kondr.gateway.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Конфигурация для настройки ограничения частоты запросов (Rate Limiter).
 */
@Configuration
public class RateLimiterConfiguration {

  private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
  private static final String IP_DELIMITER = ",";

  @Bean
  public KeyResolver ipKeyResolver() {
    return exchange -> {
      String ipAddresses = exchange.getRequest()
          .getHeaders()
          .getFirst(HEADER_X_FORWARDED_FOR);

      if (ipAddresses == null || ipAddresses.isBlank()) {
        // fallback, если нет прокси
        return Mono.just(
            exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress()
        );
      }

      int commaIndex = ipAddresses.indexOf(IP_DELIMITER);

      String ipAddress = ipAddresses.substring(0,
          (commaIndex != -1) ? commaIndex : ipAddresses.length());

      return Mono.just(ipAddress.trim());
    };
  }

}