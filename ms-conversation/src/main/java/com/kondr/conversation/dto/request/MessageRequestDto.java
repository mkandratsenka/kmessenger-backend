package com.kondr.conversation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для отправки сообщения в существующий диалог.
 *
 * @param content содержимое сообщения.
 */
public record MessageRequestDto(

    @NotBlank(message = "Содержимое сообщения не должно быть пустым")
    @Size(max = 1024, message = "Максимальная длина сообщения 1024 символа")
    String content) {

}