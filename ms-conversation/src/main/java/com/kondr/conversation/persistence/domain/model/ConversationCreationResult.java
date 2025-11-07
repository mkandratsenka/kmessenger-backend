package com.kondr.conversation.persistence.domain.model;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.MessageDocument;
import lombok.Builder;

/**
 * Результат операции создания нового диалога.
 *
 * @param conversation            созданный документ диалога.
 * @param userConversationPreview созданный документ превью диалога для пользователя,
 *                                инициировавшего чат.
 * @param message                 первое сообщение, отправленное в диалоге.
 */
@Builder
public record ConversationCreationResult(

    ConversationDocument conversation,
    ConversationPreviewDocument userConversationPreview,
    MessageDocument message) {

}