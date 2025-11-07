package com.kondr.mongo.factory.impl;

import com.kondr.mongo.factory.ObjectIdFactory;
import org.bson.types.ObjectId;

public class DefaultObjectIdFactory implements ObjectIdFactory {

  @Override
  public ObjectId newObjectId() {
    return new ObjectId();
  }

  @Override
  public String newObjectIdHex() {
    return new ObjectId().toHexString();
  }
}
