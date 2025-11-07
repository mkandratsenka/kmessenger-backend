package com.kondr.conversation.messaging.redis.event;

import java.time.Instant;
import lombok.Builder;

/**
 * Событие, публикуемое в Redis для уведомления подключенных через RSocket клиентов о появлении
 * нового сообщения.
 *
 * @param messageId      ID сообщения.
 * @param conversationId ID диалога.
 * @param senderId       ID отправителя сообщения.
 * @param preview        сокращенное содержимое сообщения для отображения в превью диалога.
 * @param messageDate    дата и время создания сообщения.
 * @param seq            порядковый номер сообщения в диалоге.
 */
@Builder(toBuilder = true)
public record MessageCreatedEvent(

    String messageId,
    String conversationId,
    String senderId,
    String preview,
    Instant messageDate,
    Long seq) {

}