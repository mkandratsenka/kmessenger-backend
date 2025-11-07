package com.kondr.conversation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для создания нового диалога.
 *
 * @param interlocutorId        ID собеседника, с которым создаётся диалог.
 * @param initialMessageContent содержимое первого сообщения в новом диалоге.
 */
public record ConversationRequestDto(

    @NotBlank(message = "ID собеседника не может быть пустым")
    String interlocutorId,

    @NotBlank(message = "Содержимое сообщения не должно быть пустым")
    @Size(max = 1024, message = "Максимальная длина сообщения 1024 символа")
    String initialMessageContent) {

}