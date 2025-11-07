package com.kondr.web.interceptor;

import com.kondr.shared.constant.CommonErrorMessages;
import com.kondr.shared.dto.ErrorResponseDto;
import com.kondr.shared.exception.InvalidRequestException;
import com.kondr.shared.utils.AppStringUtils;
import java.time.Instant;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;

/**
 * Глобальный обработчик исключений для REST API.
 */
@RestControllerAdvice
public class GlobalRestExceptionHandler {

  @ExceptionHandler(InvalidRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ErrorResponseDto> handleAsBadRequest(
      InvalidRequestException e, ServerHttpRequest request) {

    return Mono.just(buildErrorResponseDto(request, HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Mono<ErrorResponseDto> handleAsNotFound(NoResourceFoundException e,
      ServerHttpRequest req) {
    return Mono.just(buildErrorResponseDto(req, HttpStatus.NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Mono<ErrorResponseDto> handleAsInternalServerError(
      Throwable throwable, ServerHttpRequest request) {

    return Mono.just(
        buildErrorResponseDto(request, HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage()));
  }

  private ErrorResponseDto buildErrorResponseDto(ServerHttpRequest request,
      HttpStatus httpStatus, String message) {

    return ErrorResponseDto.builder()
        .path(extractPath(request))
        .timestamp(Instant.now())
        .status(httpStatus.value())
        .error(Objects.toString(message, CommonErrorMessages.UNEXPECTED_ERROR))
        .build();
  }

  private String extractPath(ServerHttpRequest request) {
    return request == null
        ? AppStringUtils.EMPTY
        : request.getURI().getPath();
  }

}