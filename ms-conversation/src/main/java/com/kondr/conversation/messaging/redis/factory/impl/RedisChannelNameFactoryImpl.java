package com.kondr.conversation.messaging.redis.factory.impl;

import com.kondr.conversation.messaging.redis.factory.RedisChannelNameFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RedisChannelNameFactoryImpl implements RedisChannelNameFactory {

  private static final String USER_MESSAGE_EVENTS_KEY_PATTERN = "chat:users:%s:messages:events";

  @Override
  public String getMessageEventsChannel(String userId) {
    Assert.hasText(userId, "userId cannot be empty");
    return USER_MESSAGE_EVENTS_KEY_PATTERN.formatted(userId);
  }

}