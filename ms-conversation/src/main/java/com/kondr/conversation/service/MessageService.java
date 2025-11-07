package com.kondr.conversation.service;

import com.kondr.conversation.dto.request.MessageRequestDto;
import com.kondr.conversation.dto.response.NewMessageResponseDto;
import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import reactor.core.publisher.Mono;

/**
 * Интеграционный сервис для работы с сообщениями.
 */
public interface MessageService {

  /**
   * Отправляет новое сообщение для указанного диалога.
   *
   * @param conversationId ID диалога.
   * @param messageDto     DTO сообщения.
   * @return {@link Mono} с DTO ответа на новое сообщение.
   */
  Mono<NewMessageResponseDto> sendMessage(String conversationId, MessageRequestDto messageDto);

  /**
   * Обрабатывает событие нового сообщения.
   *
   * @param newMessageEvent событие нового сообщения.
   * @return {@link Mono}, сигнализирующий о завершении обработки.
   */
  Mono<Void> processNewMessageEvent(NewMessageEvent newMessageEvent);

}