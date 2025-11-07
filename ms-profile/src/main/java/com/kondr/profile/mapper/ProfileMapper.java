package com.kondr.profile.mapper;

import com.kondr.profile.dto.FullProfileResponseDto;
import com.kondr.profile.dto.ProfileRequestDto;
import com.kondr.profile.persistence.domain.ProfileDocument;
import com.kondr.shared.dto.profile.PublicProfileResponseDto;
import com.kondr.web.security.model.KeycloakUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования объектов профилей.
 */
@Mapper(componentModel = "spring")
public interface ProfileMapper {

  FullProfileResponseDto toFullDto(ProfileDocument profile);

  PublicProfileResponseDto toPublicDto(ProfileDocument profile);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "usernameLower", ignore = true)
  @Mapping(target = "fullNameLower", ignore = true)
  ProfileDocument from(KeycloakUser keycloakUser, ProfileRequestDto profileDto);

}