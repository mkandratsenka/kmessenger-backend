package com.kondr.web.properties;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * Свойства для конфигурации CORS.
 */
@Getter
@Setter
@ConfigurationProperties("app.web.cors")
public class CorsProperties {

  /**
   * Флаг, разрешающий передачу учетных данных (cookies, авторизация).
   */
  private boolean allowCredentials = true;

  /**
   * Список разрешенных источников (доменов), с которых разрешены запросы.
   */
  private List<String> allowedOrigins = List.of();

  /**
   * Список разрешенных HTTP-методов (например, GET, POST).
   */
  private List<String> allowedMethods = List.of("*");

  /**
   * Список разрешенных заголовков запроса.
   */
  private List<String> allowedHeaders = List.of("*");

  /**
   * Выполняет валидацию после инициализации свойств.
   *
   * @throws IllegalArgumentException если {@code allowedOrigins} пуст.
   */
  @PostConstruct
  public void validate() {
    Assert.notEmpty(allowedOrigins, "CORS: allowedOrigins must not be empty");
  }

}