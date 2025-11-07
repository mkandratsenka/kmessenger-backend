package com.kondr.web.validation.exception;

import com.kondr.web.validation.model.ValidationError;
import java.util.List;
import lombok.Getter;

/**
 * Исключение, сигнализирующее о невалидных данных, полученных от клиента.
 */
public class FieldValidationException extends RuntimeException {

  @Getter
  private final List<ValidationError> errors;

  public FieldValidationException(List<ValidationError> errors) {
    this.errors = errors;
  }

}