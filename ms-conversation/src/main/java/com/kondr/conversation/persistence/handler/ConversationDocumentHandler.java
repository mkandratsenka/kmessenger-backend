package com.kondr.conversation.persistence.handler;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import reactor.core.publisher.Mono;

/**
 * Обработчик, выполняющий подготовку документа {@link ConversationDocument} перед сохранением в
 * базу данных.
 */
public interface ConversationDocumentHandler {

  /**
   * Выполняет подготовку документа диалога.
   *
   * @param conversation документ диалога.
   * @return {@link Mono} с подготовленным документом диалога.
   */
  Mono<ConversationDocument> handle(ConversationDocument conversation);

}