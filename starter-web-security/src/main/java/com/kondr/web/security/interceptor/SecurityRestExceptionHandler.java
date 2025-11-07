package com.kondr.web.security.interceptor;

import com.kondr.web.security.exception.UnauthorizedUserException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * Глобальный обработчик исключений, связанных с безопасностью.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityRestExceptionHandler {

  @ExceptionHandler(UnauthorizedUserException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Mono<?> handleAsUnauthorized() {
    return Mono.empty();
  }

}