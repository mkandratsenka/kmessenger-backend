package com.kondr.conversation.mapper;

import com.kondr.conversation.dto.response.InterlocutorPreviewDto;
import com.kondr.conversation.persistence.domain.embedded.InterlocutorPreview;
import com.kondr.shared.dto.profile.PublicProfileResponseDto;
import org.mapstruct.Mapper;

/**
 * Маппер для преобразования объектов профилей собеседников.
 */
@Mapper(componentModel = "spring")
public interface InterlocutorPreviewMapper {

  InterlocutorPreview from(PublicProfileResponseDto publicProfileDto);

  InterlocutorPreviewDto toDto(InterlocutorPreview interlocutorPreview);

}
