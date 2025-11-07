package com.kondr.conversation.dto.response;

import java.time.Instant;
import lombok.Builder;

/**
 * DTO, представляющий собой собранные данные из диалога и превью диалога пользователя.
 *
 * @param id                 ID превью диалога.
 * @param conversationId     ID диалога.
 * @param interlocutor       превью собеседника.
 * @param readMessageSeq     порядковый номер последнего прочитанного сообщения.
 * @param lastMessagePreview краткое содержимое последнего отправленного сообщения.
 * @param lastMessageDate    дата последнего отправленного сообщения.
 * @param lastMessageSeq     порядковый номер последнего отправленного сообщения.
 */
@Builder
public record FullConversationPreviewResponseDto(

    String id,
    String conversationId,
    InterlocutorPreviewDto interlocutor,
    Long readMessageSeq,

    String lastMessagePreview,
    Instant lastMessageDate,
    Long lastMessageSeq) {

}