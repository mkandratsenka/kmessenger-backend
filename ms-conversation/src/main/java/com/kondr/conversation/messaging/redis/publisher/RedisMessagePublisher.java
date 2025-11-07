package com.kondr.conversation.messaging.redis.publisher;

import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import reactor.core.publisher.Mono;

/**
 * Интерфейс для публикации событий сообщений в Redis.
 */
public interface RedisMessagePublisher {

  /**
   * Публикует событие создания сообщения для пользователя.
   *
   * @param event  событие создания сообщения.
   * @param userId ID пользователя.
   * @return {@link Mono} с количеством получателей.
   */
  Mono<Long> publish(MessageCreatedEvent event, String userId);

}