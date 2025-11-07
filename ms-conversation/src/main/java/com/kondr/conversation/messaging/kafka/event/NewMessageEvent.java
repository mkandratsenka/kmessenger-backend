package com.kondr.conversation.messaging.kafka.event;

import java.time.Instant;
import lombok.Builder;

/**
 * Событие, публикуемое в Kafka при отправке нового сообщения.
 *
 * @param tempId         временный ID.
 * @param conversationId ID диалога.
 * @param senderId       ID отправителя.
 * @param content        содержимое сообщения.
 * @param sentAt         дата и время отправки сообщения.
 */
@Builder(toBuilder = true)
public record NewMessageEvent(

    String tempId,
    String conversationId,
    String senderId,
    String content,
    Instant sentAt) {

}