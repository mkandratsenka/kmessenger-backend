package com.kondr.conversation.mapper;

import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.domain.MessageDocument;
import com.kondr.mongo.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования объектов событий, связанных с сообщениями.
 */
@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class})
public interface MessageEventMapper {

  @Mapping(source = "message.id", target = "messageId")
  @Mapping(source = "conversation.id", target = "conversationId")
  @Mapping(source = "message.senderId", target = "senderId")
  @Mapping(source = "conversation.lastMessagePreview", target = "preview")
  @Mapping(source = "conversation.lastMessageDate", target = "messageDate")
  @Mapping(source = "conversation.lastMessageSeq", target = "seq")
  MessageCreatedEvent toMessageCreatedEvent(MessageDocument message,
      ConversationDocument conversation);

}
