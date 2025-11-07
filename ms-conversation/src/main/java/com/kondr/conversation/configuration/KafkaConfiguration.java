package com.kondr.conversation.configuration;

import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

/**
 * Конфигурация для создания и настройки реактивных компонентов Kafka.
 */
@Configuration
public class KafkaConfiguration {

  @Bean
  public ReactiveKafkaProducerTemplate<String, NewMessageEvent> messageProducerTemplate(
      KafkaProperties kafkaProperties,
      @Value("${app.kafka.producers.message.delivery-timeout-ms}") int deliveryTimeoutMs,
      @Value("${app.kafka.producers.message.request-timeout-ms}") int requestTimeoutMs,
      @Value("${app.kafka.producers.message.max-block-ms}") int maxBlockMs) {

    Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties();
    producerProperties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
    producerProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
    producerProperties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockMs);

    return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(producerProperties));
  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, NewMessageEvent> messageConsumerTemplate(
      KafkaProperties properties,
      @Value("${app.kafka.topics.messages}") String messagesTopic,
      @Value("${app.kafka.consumers.message.commit-retry-interval}") Duration commitRetryInterval,
      @Value("${app.kafka.consumers.message.max-deferred-commits}") int maxDeferredCommits) {

    var receiverOptions = ReceiverOptions
        .<String, NewMessageEvent>create(properties.buildConsumerProperties())
        .commitRetryInterval(commitRetryInterval)
        .maxDeferredCommits(maxDeferredCommits)
        .subscription(List.of(messagesTopic));

    return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
  }

}