package com.kondr.conversation.mapper;

import com.kondr.conversation.dto.response.MessageResponseDto;
import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import com.kondr.conversation.persistence.domain.MessageDocument;
import com.kondr.mongo.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования объектов сообщений.
 */
@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface MessageMapper {

  MessageResponseDto toDto(MessageDocument message);

  @Mapping(source = "tempId", target = "id")
  @Mapping(target = "seq", ignore = true)
  MessageDocument from(NewMessageEvent event);

}