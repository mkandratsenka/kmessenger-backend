package com.kondr.conversation.messaging.kafka.producer.impl;

import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import com.kondr.conversation.messaging.kafka.producer.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageProducerImpl implements KafkaMessageProducer {

  @Value("${app.kafka.topics.messages}")
  private String messagesTopic;

  private final ReactiveKafkaProducerTemplate<String, NewMessageEvent> messageProducerTemplate;

  @Override
  public Mono<Void> send(NewMessageEvent newMessageEvent) {
    log.info("Sending newMessageEvent {}", newMessageEvent.tempId());
    return messageProducerTemplate
        .send(messagesTopic, newMessageEvent.conversationId(), newMessageEvent)
        .then();
  }

}