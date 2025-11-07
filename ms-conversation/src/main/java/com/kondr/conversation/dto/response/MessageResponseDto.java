package com.kondr.conversation.dto.response;

import java.time.Instant;
import lombok.Builder;

/**
 * DTO сообщения.
 *
 * @param id       ID сообщения.
 * @param senderId ID отправителя сообщения.
 * @param sentAt   дата и время отправки сообщения.
 * @param seq      порядковый номер сообщения в диалоге.
 * @param content  содержимое сообщения.
 */
@Builder
public record MessageResponseDto(

    String id,
    String senderId,
    Instant sentAt,
    Long seq,
    String content) {

}