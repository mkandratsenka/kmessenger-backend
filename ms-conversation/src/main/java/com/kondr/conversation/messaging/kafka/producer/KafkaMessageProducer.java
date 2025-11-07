package com.kondr.conversation.messaging.kafka.producer;

import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import reactor.core.publisher.Mono;

/**
 * Интерфейс для публикации событий сообщений в Kafka.
 */
public interface KafkaMessageProducer {

  /**
   * Отправляет событие нового сообщения в Kafka.
   *
   * @param newMessageEvent событие нового сообщения.
   * @return {@link Mono}, сигнализирующий о завершении отправки события в Kafka.
   */
  Mono<Void> send(NewMessageEvent newMessageEvent);

}