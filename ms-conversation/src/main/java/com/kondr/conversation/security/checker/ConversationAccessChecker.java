package com.kondr.conversation.security.checker;

import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;

/**
 * Отвечает за проверку доступа к диалогам.
 */
public interface ConversationAccessChecker {

  /**
   * Проверяет, имеет ли пользователь, связанный с данным RSocket-соединением, доступ к диалогу.
   *
   * @param conversationId ID диалога.
   * @param requester      RSocket-соедиение.
   * @return {@link Mono} с {@code true}, если доступ разрешён.
   */
  Mono<Boolean> hasAccess(String conversationId, RSocketRequester requester);

}