package com.kondr.conversation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

/**
 * Конфигурация безопасности для RSocket, обеспечивающая авторизацию и аутентификацию.
 */
@Configuration
@EnableRSocketSecurity
public class RSocketSecurityConfiguration {

  @Bean
  public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket) {
    return rsocket
        .authorizePayload(authorize ->
            authorize
                .anyExchange().authenticated()
        )
        .jwt(Customizer.withDefaults())
        .build();
  }

}