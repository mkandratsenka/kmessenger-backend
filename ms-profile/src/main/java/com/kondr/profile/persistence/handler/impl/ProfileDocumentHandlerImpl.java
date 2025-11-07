package com.kondr.profile.persistence.handler.impl;

import com.kondr.profile.persistence.domain.ProfileDocument;
import com.kondr.profile.persistence.handler.ProfileDocumentHandler;
import com.kondr.profile.persistence.validator.ProfileDocumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProfileDocumentHandlerImpl implements ProfileDocumentHandler {

  private final ProfileDocumentValidator profileDocumentValidator;

  @Override
  public Mono<ProfileDocument> handle(ProfileDocument profile) {
    return profileDocumentValidator.validate(profile)
        .map(this::prepareSearchFields);
  }

  private ProfileDocument prepareSearchFields(ProfileDocument profile) {
    profile.setUsernameLower(profile.getUsername().toLowerCase());
    profile.setFullNameLower((profile.getLastName() + " " + profile.getFirstName()).toLowerCase());
    return profile;
  }

}