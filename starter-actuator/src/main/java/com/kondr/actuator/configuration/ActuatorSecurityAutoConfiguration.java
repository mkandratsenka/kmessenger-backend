package com.kondr.actuator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

/**
 * Автоматическая конфигурация, необходимая для настройки и защиты эндпоинтов Actuator.
 */
@AutoConfiguration(after = ReactiveUserDetailsServiceAutoConfiguration.class)
public class ActuatorSecurityAutoConfiguration {

  public static final String ACTUATOR_PASSWORD_ENCODER_NAME = "actuatorPasswordEncoder";
  public static final String ACTUATOR_SECURITY_WEB_FILTER_CHAIN_NAME = "actuatorSecurityWebFilterChain";

  @Bean(ACTUATOR_PASSWORD_ENCODER_NAME)
  @ConditionalOnMissingBean(name = ACTUATOR_PASSWORD_ENCODER_NAME)
  public PasswordEncoder actuatorPasswordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Order(1)
  @Bean(ACTUATOR_SECURITY_WEB_FILTER_CHAIN_NAME)
  @ConditionalOnMissingBean(name = ACTUATOR_SECURITY_WEB_FILTER_CHAIN_NAME)
  public SecurityWebFilterChain actuatorSecurityWebFilterChain(ServerHttpSecurity httpSecurity,
      @Value("${app.actuator.security.username}") String username,
      @Value("${app.actuator.security.password}") String password) {

    PasswordEncoder encoder = actuatorPasswordEncoder();
    String encoded = encoder.encode(password);

    var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
        new MapReactiveUserDetailsService(
            User.withUsername(username).password(encoded).roles("ACTUATOR").build()
        )
    );
    authenticationManager.setPasswordEncoder(encoder);

    return httpSecurity
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
        .authorizeExchange(exchange ->
            exchange
                .anyExchange().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .authenticationManager(authenticationManager)
        .build();
  }

}