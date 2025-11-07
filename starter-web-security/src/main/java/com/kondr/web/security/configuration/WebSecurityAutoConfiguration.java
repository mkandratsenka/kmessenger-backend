package com.kondr.web.security.configuration;

import com.kondr.web.security.interceptor.SecurityRestExceptionHandler;
import com.kondr.web.security.service.CurrentUserService;
import com.kondr.web.security.service.impl.CurrentUserServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Автоматическая конфигурация, предоставляющая инфраструктуру для настройки и обеспечения
 * безопасности веб-приложения.
 */
@AutoConfiguration
public class WebSecurityAutoConfiguration {

  public static final String SECURITY_WEB_FILTER_CHAIN_NAME = "securityWebFilterChain";

  @Bean(SECURITY_WEB_FILTER_CHAIN_NAME)
  @ConditionalOnMissingBean(name = SECURITY_WEB_FILTER_CHAIN_NAME)
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
    return serverHttpSecurity
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(exchange ->
            exchange
                .anyExchange().authenticated()
        )
        .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  @ConditionalOnMissingBean
  public CurrentUserService currentUserService() {
    return new CurrentUserServiceImpl();
  }

  @Bean
  @ConditionalOnProperty(prefix = "app.web.exception-handler.security", name = "enabled", havingValue = "true", matchIfMissing = true)
  @ConditionalOnMissingBean(SecurityRestExceptionHandler.class)
  public SecurityRestExceptionHandler securityRestExceptionHandler() {
    return new SecurityRestExceptionHandler();
  }

}