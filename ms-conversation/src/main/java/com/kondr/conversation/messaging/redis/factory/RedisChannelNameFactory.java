package com.kondr.conversation.messaging.redis.factory;

/**
 * Фабрика для генерации имён каналов в Redis.
 */
public interface RedisChannelNameFactory {

  /**
   * Возвращает имя канала для (Pub/Sub) событий сообщений указанного пользователя.
   *
   * @param userId ID пользователя.
   * @return имя канала для (Pub/Sub) событий сообщений пользователя.
   */
  String getMessageEventsChannel(String userId);

}