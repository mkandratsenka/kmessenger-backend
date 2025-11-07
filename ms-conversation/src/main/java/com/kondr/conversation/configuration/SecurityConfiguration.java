package com.kondr.conversation.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;

/**
 * Общая конфигурация для настройки безопасности приложения.
 * <p>
 * Включает возможность защиты реактивных методов с помощью {@link PreAuthorize} и т.д.
 */
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfiguration {

}