package com.kondr.conversation.manager;

import com.kondr.conversation.context.RSocketConnectionContext;
import org.springframework.messaging.rsocket.RSocketRequester;

/**
 * Менеджер для управления контекстами текущих RSocket-соединений.
 */
public interface RSocketConnectionContextManager {

  /**
   * Создает и регистрирует контекст для указанного RSocket-соединения.
   *
   * @param requester RSocket-соединение.
   */
  void register(RSocketRequester requester);

  /**
   * Получение контекста для указанного RSocket-соединения.
   *
   * @param requester RSocket-соединение.
   */
  RSocketConnectionContext getContext(RSocketRequester requester);

  /**
   * Удаляет контекст (отменяет регистрацию) для указанного RSocket-соединения.
   *
   * @param requester RSocket-соединение.
   */
  void unregister(RSocketRequester requester);

}