package com.kondr.shared.dto;

import java.time.Instant;
import lombok.Builder;

/**
 * DTO для ответов об ошибках.
 *
 * @param path      путь запроса, где произошла ошибка.
 * @param timestamp время ошибки.
 * @param status    HTTP-статус ошибки.
 * @param error     описание ошибки.
 */
@Builder
public record ErrorResponseDto(

    String path,
    Instant timestamp,
    Integer status,
    String error) {

}