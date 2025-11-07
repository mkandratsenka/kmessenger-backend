package com.kondr.conversation.mapper;


import com.kondr.conversation.dto.response.ConversationCreatedResponseDto;
import com.kondr.conversation.persistence.domain.model.ConversationCreationResult;
import com.kondr.mongo.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper для преобразования сущностей и DTO, связанных с диалогами.
 *
 * <p>
 * MapStruct автоматически не внедряет зависимости, указанные в {@code uses}, если они используются
 * только с помощью {@code @Mapping(expression)}.
 * </p>
 */
@Mapper(componentModel = "spring", uses = {MessageMapper.class, ObjectIdMapper.class})
public abstract class ConversationMapper {

  @Autowired
  protected ConversationPreviewMapper conversationPreviewMapper;

  @Mapping(target = "conversationPreview", expression = "java(conversationPreviewMapper.toFullDto(result.conversation(), result.userConversationPreview()))")
  @Mapping(source = "message", target = "message")
  public abstract ConversationCreatedResponseDto toDto(ConversationCreationResult result);

}