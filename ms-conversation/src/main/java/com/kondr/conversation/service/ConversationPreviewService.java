package com.kondr.conversation.service;

import java.time.Instant;
import reactor.core.publisher.Mono;

/**
 * Интеграционный сервис для работы с превью диалогов.
 */
public interface ConversationPreviewService {

  /**
   * Фиксирует порядковый номер (sequence number) последнего прочитанного сообщения для указанного
   * превью диалога пользователя.
   *
   * @param id      ID превью диалога.
   * @param readSeq порядковый номер последнего прочитанного сообщения.
   * @return {@link Mono} с датой и временем в случае успеха обновления счетчика последнего
   * прочитанного сообщения.
   */
  Mono<Instant> commitReadSeq(String id, Long readSeq);

}