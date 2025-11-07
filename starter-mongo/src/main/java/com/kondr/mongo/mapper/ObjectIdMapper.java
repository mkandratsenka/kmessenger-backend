package com.kondr.mongo.mapper;

import org.bson.types.ObjectId;

/**
 * Маппер для {@link ObjectId}.
 */
public class ObjectIdMapper {

  /**
   * Преобразует {@link ObjectId} в его строковое представление в шестнадцатеричном формате.
   *
   * @param objectId ID для преобразования.
   * @return строковое представление ID в шестнадцатеричном формате.
   */
  public String toString(ObjectId objectId) {
    return objectId != null ? objectId.toHexString() : null;
  }

  /**
   * Преобразует строковое представление ID в {@link ObjectId}.
   *
   * @param id строка с ID в шестнадцатеричном формате.
   * @return экземпляр {@link ObjectId}.
   */
  public ObjectId toObjectId(String id) {
    return id != null ? new ObjectId(id) : null;
  }

}