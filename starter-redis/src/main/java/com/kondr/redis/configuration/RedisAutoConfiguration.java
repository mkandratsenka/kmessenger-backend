package com.kondr.redis.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Автоматическая конфигурация, настраивающая инфраструктуру для работы с Redis в реактивном стиле.
 */
@AutoConfiguration
public class RedisAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    objectMapper = objectMapper.activateDefaultTyping(
        objectMapper.getPolymorphicTypeValidator(),
        DefaultTyping.EVERYTHING,
        JsonTypeInfo.As.PROPERTY
    );

    return new GenericJackson2JsonRedisSerializer(objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory connectionFactory,
      GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {

    var serializationContext = RedisSerializationContext
        .<String, Object>newSerializationContext(new StringRedisSerializer())
        .value(genericJackson2JsonRedisSerializer)
        .build();

    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }

}