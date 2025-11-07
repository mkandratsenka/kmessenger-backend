package com.kondr.profile.persistence.handler;

import com.kondr.profile.persistence.domain.ProfileDocument;
import reactor.core.publisher.Mono;

/**
 * Обработчик, выполняющий подготовку документа {@link ProfileDocument} перед сохранением в базу
 * данных.
 */
public interface ProfileDocumentHandler {

  /**
   * Выполняет подготовку документа профиля.
   *
   * @param profile документ профиля.
   * @return {@link Mono} с подготовленным документом профиля.
   */
  Mono<ProfileDocument> handle(ProfileDocument profile);

}