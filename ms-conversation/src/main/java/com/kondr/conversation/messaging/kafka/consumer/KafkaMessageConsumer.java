package com.kondr.conversation.messaging.kafka.consumer;

import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import com.kondr.conversation.service.MessageService;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.retry.Retry;

/**
 * Реактивный Kafka-консьюмер для обработки событий сообщений.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {

  private final ReactiveKafkaConsumerTemplate<String, NewMessageEvent> messageConsumerTemplate;

  private final MessageService messageService;

  @Value("${app.kafka.consumers.message.events-prefetch}")
  private int eventsPrefetch;

  @Value("${app.kafka.consumers.message.max-active-conversations}")
  private int maxActiveConversations;

  @Value("${app.kafka.consumers.message.process-timeout}")
  private Duration messageEventProcessTimeout;

  @Value("${app.kafka.consumers.message.conversation-idle-timeout}")
  private Duration conversationIdleTimeout;

  @Value("${app.kafka.consumers.message.conversation-idle-check-interval}")
  private Duration conversationIdleCheckInterval;

  /**
   * {@code concatMap} необходим для последовательного сохранения сообщений в рамках одного диалога,
   * для избежания Write Conflict в MongoDB и более предсказуемого порядка.
   * <p>
   * С помощью {@code lastActivity} отслеживаются неактивные диалоги для их последующего удаления.
   * Если не подчищать - буфер со временем заполнится и будет Deadlock.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void startConsuming() {
    log.info("Starting kafka message consumer");

    messageConsumerTemplate.receive()
        .groupBy(ConsumerRecord::key, eventsPrefetch)
        .flatMap(conversationStream ->
                conversationStream.transformDeferred(transformedConversationStream -> {
                      var lastActivity = new AtomicReference<>(Instant.now());

                      return transformedConversationStream
                          .concatMap(record -> consumeNewMessageEvent(record, lastActivity))
                          .mergeWith(Flux.interval(conversationIdleCheckInterval)
                              .filter(i -> isConversationIdle(lastActivity))
                              .take(1)
                              .flatMap(i -> Mono.error(new TimeoutException()))
                          );
                    })
                    .onErrorResume(this::handleConversationStreamError),
            maxActiveConversations
        )
        .subscribe();
  }

  private Mono<Void> consumeNewMessageEvent(ReceiverRecord<String, NewMessageEvent> record,
      AtomicReference<Instant> lastActivity) {

    AtomicBoolean isProcessFinished = new AtomicBoolean(false);

    return Mono.just(record.value())
        .flatMap(newMessageEvent -> {
          lastActivity.set(Instant.now());
          return messageService.processNewMessageEvent(newMessageEvent);
        })
        .timeout(messageEventProcessTimeout)
        .retryWhen(Retry.indefinitely()
            .filter(throwable -> throwable instanceof TimeoutException)
        )
        .doOnSuccess(v -> {
          acknowledgeProcessing(record, lastActivity, isProcessFinished);
          log.info("Successfully processed newMessageEvent {}", record.value().tempId());
        })
        .onErrorResume(throwable -> {
          // пока без DLQ
          acknowledgeProcessing(record, lastActivity, isProcessFinished);
          log.error("Error processing newMessageEvent {}", record.value().tempId(), throwable);
          return Mono.empty();
        })
        .doOnCancel(() -> {
          // если гонка при при подчистке группы
          if (!isProcessFinished.get()) {
            // пока без DLQ
            record.receiverOffset().acknowledge(); // избежать deadlock из-за max-deferred-commits
            log.warn("Cancelling processing newMessageEvent {}", record.value().tempId());
          }
        });
  }

  private boolean isConversationIdle(AtomicReference<Instant> lastActivity) {
    return Duration.between(lastActivity.get(), Instant.now())
        .compareTo(conversationIdleTimeout) > 0;
  }

  private void acknowledgeProcessing(ReceiverRecord<String, NewMessageEvent> record,
      AtomicReference<Instant> lastActivity, AtomicBoolean isProcessFinished) {

    record.receiverOffset().acknowledge();
    lastActivity.set(Instant.now());
    isProcessFinished.set(true);
  }

  private Mono<Void> handleConversationStreamError(Throwable throwable) {
    if (!(throwable instanceof TimeoutException)) {
      log.error("Unhandled exception in conversation stream.", throwable);
    }
    return Mono.empty();
  }

}