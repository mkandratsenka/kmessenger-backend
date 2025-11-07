package com.kondr.conversation.security.checker;

import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;

/**
 * Отвечает за проверку доступа к превью диалогов.
 */
public interface ConversationPreviewAccessChecker {

  /**
   * Проверяет, имеет ли пользователь, связанный с данным RSocket-соединением, доступ к превью
   * диалога.
   *
   * @param conversationPreviewId ID превью диалога.
   * @param requester             RSocket-соедиение.
   * @return {@link Mono} с {@code true}, если доступ разрешён.
   */
  Mono<Boolean> hasAccess(String conversationPreviewId, RSocketRequester requester);

}