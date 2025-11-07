package com.kondr.web.validation.interceptor;


import com.kondr.web.validation.dto.ValidationResponseDto;
import com.kondr.web.validation.exception.FieldValidationException;
import com.kondr.web.validation.model.ValidationError;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

/**
 * Глобальный обработчик исключений для преобразования ошибок валидации полей в стандартизированный
 * DTO ответа об ошибке.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationRestExceptionHandler {

  @ExceptionHandler(FieldValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ValidationResponseDto> handleAsBadRequest(FieldValidationException e) {
    return Mono.just(new ValidationResponseDto(e.getErrors()));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ValidationResponseDto> handleAsBadRequest(WebExchangeBindException e) {
    List<ValidationError> validationErrors = e.getFieldErrors().stream()
        .collect(Collectors.groupingBy(
            FieldError::getField,
            Collectors.mapping(
                fieldError -> StringUtils.defaultString(fieldError.getDefaultMessage()),
                Collectors.toList()
            )
        ))
        .entrySet().stream()
        .map(entry -> new ValidationError(entry.getKey(), entry.getValue()))
        .toList();

    return Mono.just(new ValidationResponseDto(validationErrors));
  }
}