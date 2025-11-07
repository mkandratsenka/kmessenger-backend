package com.kondr.conversation.persistence.validator;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import reactor.core.publisher.Mono;

/**
 * Валидатор для проверки документов диалогов.
 */
public interface ConversationDocumentValidator {

  /**
   * Выполняет валидацию документа диалога.
   *
   * @param conversation документ диалога.
   * @return {@link Mono} с проверенным документом диалога или ошибкой валидации.
   */
  Mono<ConversationDocument> validate(ConversationDocument conversation);

}
