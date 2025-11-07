package com.kondr.profile.persistence.service.impl;

import com.kondr.profile.dto.ProfileRequestDto;
import com.kondr.profile.mapper.ProfileMapper;
import com.kondr.profile.persistence.dao.ProfileDocumentDAO;
import com.kondr.profile.persistence.domain.ProfileDocument;
import com.kondr.profile.persistence.service.ProfileDocumentService;
import com.kondr.profile.validation.validator.ProfileValidator;
import com.kondr.web.security.constant.AuthConstants;
import com.kondr.web.security.service.CurrentUserService;
import com.kondr.web.validation.exception.FieldValidationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProfileDocumentServiceImpl implements ProfileDocumentService {

  private static final int PROFILE_FETCH_LIMIT = 10;

  private final ProfileDocumentDAO profileDocumentDAO;

  private final CurrentUserService currentUserService;

  private final ProfileValidator profileValidator;

  private final ProfileMapper profileMapper;

  @Override
  public Mono<ProfileDocument> create(ProfileRequestDto profileDto) {
    return profileValidator.validate(profileDto)
        .flatMap(errors -> !CollectionUtils.isEmpty(errors)
            ? Mono.error(new FieldValidationException(errors))
            : currentUserService.getKeycloakUserOrError()
        )
        .map(keycloakUser -> profileMapper.from(keycloakUser, profileDto))
        .flatMap(profileDocumentDAO::create);
  }

  @Override
  public Mono<ProfileDocument> findCurrentProfile() {
    return currentUserService.getId()
        .flatMap(profileDocumentDAO::findById);
  }

  @Override
  public Flux<ProfileDocument> findByIds(List<String> ids) {
    return profileDocumentDAO.findByIds(ids);
  }

  @Override
  public Flux<ProfileDocument> findProfiles(String username, List<String> nameTokens) {
    return currentUserService.getId()
        .defaultIfEmpty(AuthConstants.UNAUTHENTICATED_USER_ID)
        .flatMapMany(currentUserId ->
            profileDocumentDAO
                .findProfiles(username, nameTokens, currentUserId, PROFILE_FETCH_LIMIT)
        );
  }

}