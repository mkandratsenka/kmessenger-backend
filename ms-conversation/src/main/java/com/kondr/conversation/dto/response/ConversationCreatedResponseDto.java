package com.kondr.conversation.dto.response;

import lombok.Builder;

/**
 * DTO для ответа после успешного создания нового диалога.
 *
 * @param conversationPreview полное превью созданного диалога для текущего пользователя.
 * @param message             первое сообщение, отправленное в диалоге.
 */
@Builder
public record ConversationCreatedResponseDto(

    FullConversationPreviewResponseDto conversationPreview,
    MessageResponseDto message) {

}