package com.kondr.conversation.dto.response;

import java.time.Instant;
import lombok.Builder;

/**
 * DTO для ответа на запрос отправки нового сообщения.
 *
 * @param sentAt дата и время, в которое сообщение было обработано сервером.
 * @param tempId временный ID сообщения.
 */
@Builder
public record NewMessageResponseDto(

    Instant sentAt,
    String tempId) {

}