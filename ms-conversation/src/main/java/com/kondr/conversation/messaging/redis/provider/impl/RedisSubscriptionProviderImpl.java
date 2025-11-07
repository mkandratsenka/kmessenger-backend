package com.kondr.conversation.messaging.redis.provider.impl;

import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import com.kondr.conversation.messaging.redis.factory.RedisChannelNameFactory;
import com.kondr.conversation.messaging.redis.provider.RedisSubscriptionProvider;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriptionProviderImpl implements RedisSubscriptionProvider {

  private final RedisChannelNameFactory redisChannelNameFactory;

  private final ReactiveRedisTemplate<String, Object> redisTemplate;

  private final Map<String, Flux<MessageCreatedEvent>> userIdToStream = new ConcurrentHashMap<>();

  /**
   * Если активных подписчиков по указанному {@code userId} больше нет, то подписка в Redis
   * автоматически отменяется. Поэтому до удаления из {@link #userIdToStream} другой поток может
   * получить "canceled" поток. Крайне маловероятно и решается повторными попытками (ретраями) с
   * задержкой.
   */
  @Override
  public Flux<MessageCreatedEvent> subscribeToMessageEvents(String userId) {
    return StringUtils.isBlank(userId)
        ? Flux.error(new IllegalArgumentException("userId cannot be empty"))
        : userIdToStream.computeIfAbsent(userId, absentId ->
            redisTemplate.listenToChannel(redisChannelNameFactory.getMessageEventsChannel(userId))
                .map(message -> (MessageCreatedEvent) message.getMessage())
                .onErrorResume(throwable ->
                    throwable instanceof CancellationException
                        ? Flux.empty() // завершение upstream без лога ошибки
                        : Flux.error(throwable)
                )
                .doFinally(signalType -> userIdToStream.remove(userId))
                .publish()
                .refCount(1)
        );
  }

}