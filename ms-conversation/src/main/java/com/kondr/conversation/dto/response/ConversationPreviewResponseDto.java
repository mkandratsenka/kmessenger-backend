package com.kondr.conversation.dto.response;

import lombok.Builder;

/**
 * DTO, содержащее данные превью диалога пользователя.
 *
 * @param id             ID превью диалога.
 * @param conversationId ID диалога.
 * @param interlocutor   превью собеседника.
 * @param readMessageSeq порядковый номер последнего прочитанного сообщения.
 */
@Builder
public record ConversationPreviewResponseDto(

    String id,
    String conversationId,
    InterlocutorPreviewDto interlocutor,
    Long readMessageSeq) {

}