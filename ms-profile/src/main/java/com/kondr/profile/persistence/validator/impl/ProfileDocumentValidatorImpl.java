package com.kondr.profile.persistence.validator.impl;

import com.kondr.profile.persistence.domain.ProfileDocument;
import com.kondr.profile.persistence.validator.ProfileDocumentValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProfileDocumentValidatorImpl implements ProfileDocumentValidator {

  @Override
  public Mono<ProfileDocument> validate(ProfileDocument profile) {
    if (profile == null) {
      return Mono.error(new IllegalArgumentException("Profile document cannot be null"));
    }

    if (StringUtils.isBlank(profile.getUsername())) {
      return Mono.error(new IllegalArgumentException("username is required"));
    }

    if (StringUtils.isBlank(profile.getLastName())) {
      return Mono.error(new IllegalArgumentException("lastName is required"));
    }

    if (StringUtils.isBlank(profile.getFirstName())) {
      return Mono.error(new IllegalArgumentException("firstName is required"));
    }

    return Mono.just(profile);
  }

}
