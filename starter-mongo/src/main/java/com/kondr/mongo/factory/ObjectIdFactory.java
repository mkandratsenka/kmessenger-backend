package com.kondr.mongo.factory;

import org.bson.types.ObjectId;

/**
 * Фабрика для генерации идентификаторов MongoDB ({@link ObjectId}).
 */
public interface ObjectIdFactory {

  /**
   * Генерирует и возвращает новый уникальный {@link ObjectId}.
   *
   * @return новый экземпляр {@link ObjectId}.
   */
  ObjectId newObjectId();

  /**
   * Генерирует новый {@link ObjectId} и возвращает его шестнадцатеричное представление.
   *
   * @return шестнадцатеричное представление нового ObjectId.
   */
  String newObjectIdHex();

}