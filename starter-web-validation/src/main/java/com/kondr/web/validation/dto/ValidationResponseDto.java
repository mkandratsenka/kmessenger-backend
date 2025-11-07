package com.kondr.web.validation.dto;

import com.kondr.web.validation.model.ValidationError;
import java.util.List;

/**
 * DTO для ответа с ошибками валидации.
 *
 * @param validationErrors список ошибок валидации.
 */
public record ValidationResponseDto(List<ValidationError> validationErrors) {

}