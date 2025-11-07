package com.kondr.profile.validation.validator.impl;

import static com.kondr.profile.validation.constant.ValidationFields.USERNAME;
import static com.kondr.profile.validation.constant.ValidationMessages.USERNAME_EMPTY;
import static com.kondr.profile.validation.constant.ValidationMessages.USERNAME_TAKEN;

import com.kondr.profile.dto.ProfileRequestDto;
import com.kondr.profile.persistence.dao.ProfileDocumentDAO;
import com.kondr.profile.validation.validator.ProfileValidator;
import com.kondr.web.validation.model.ValidationError;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProfileValidatorImpl implements ProfileValidator {

  private final ProfileDocumentDAO profileDocumentDAO;

  @Override
  public Mono<List<ValidationError>> validate(ProfileRequestDto profileDto) {
    String username = profileDto != null ? profileDto.username() : null;

    return StringUtils.isBlank(username)
        ? Mono.just(List.of(new ValidationError(USERNAME, List.of(USERNAME_EMPTY))))
        : profileDocumentDAO.existsByUsername(username)
            .map(taken -> taken
                ? List.of(new ValidationError(USERNAME, List.of(USERNAME_TAKEN)))
                : List.of()
            );
  }
}