package com.kondr.conversation.messaging.redis.publisher.impl;

import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import com.kondr.conversation.messaging.redis.factory.RedisChannelNameFactory;
import com.kondr.conversation.messaging.redis.publisher.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessagePublisherImpl implements RedisMessagePublisher {

  private final RedisChannelNameFactory redisChannelNameFactory;

  private final ReactiveRedisTemplate<String, Object> redisTemplate;

  @Override
  public Mono<Long> publish(MessageCreatedEvent event, String userId) {
    log.info("Publish an event for user: {}", userId);
    return redisTemplate
        .convertAndSend(redisChannelNameFactory.getMessageEventsChannel(userId), event);
  }

}