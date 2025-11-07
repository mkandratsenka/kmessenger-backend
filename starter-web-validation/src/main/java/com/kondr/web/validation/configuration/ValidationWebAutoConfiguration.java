package com.kondr.web.validation.configuration;

import com.kondr.web.validation.interceptor.ValidationRestExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Автоматическая конфигурация, предоставляющая инфраструктуру для обработки ошибок валидации.
 */
@AutoConfiguration
public class ValidationWebAutoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "app.web.exception-handler.validation", name = "enabled", havingValue = "true", matchIfMissing = true)
  @ConditionalOnMissingBean(ValidationRestExceptionHandler.class)
  public ValidationRestExceptionHandler validationRestExceptionHandler() {
    return new ValidationRestExceptionHandler();
  }

}