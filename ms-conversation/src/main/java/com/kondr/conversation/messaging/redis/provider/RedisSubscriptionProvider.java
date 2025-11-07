package com.kondr.conversation.messaging.redis.provider;

import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import reactor.core.publisher.Flux;

/**
 * Подписывает на события в Redis.
 */
public interface RedisSubscriptionProvider {

  /**
   * Подписывается на поток событий сообщений для указанного пользователя. Возвращает Flux, который
   * эмитит события по мере их поступления из Redis.
   *
   * @param userId ID пользователя.
   * @return {@link Flux} событий {@link MessageCreatedEvent}.
   */
  Flux<MessageCreatedEvent> subscribeToMessageEvents(String userId);

}