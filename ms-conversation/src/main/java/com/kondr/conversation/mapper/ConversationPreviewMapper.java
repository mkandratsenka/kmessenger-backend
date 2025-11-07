package com.kondr.conversation.mapper;

import com.kondr.conversation.dto.response.ConversationPreviewResponseDto;
import com.kondr.conversation.dto.response.FullConversationPreviewResponseDto;
import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.projection.ConversationPreviewProjection;
import com.kondr.mongo.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования объектов, связанных с превью диалогов.
 */
@Mapper(componentModel = "spring", uses = {InterlocutorPreviewMapper.class, ObjectIdMapper.class})
public interface ConversationPreviewMapper {

  ConversationPreviewResponseDto toDto(ConversationPreviewDocument conversationPreview);

  @Mapping(source = "conversationPreview.id", target = "id")
  FullConversationPreviewResponseDto toFullDto(ConversationDocument conversation,
      ConversationPreviewDocument conversationPreview);

  @Mapping(source = "previewId", target = "id")
  FullConversationPreviewResponseDto toFullDto(ConversationPreviewProjection projection);

}