package com.kondr.profile.controller;

import com.kondr.profile.constant.ErrorMessages;
import com.kondr.profile.dto.FullProfileResponseDto;
import com.kondr.profile.dto.ProfileRequestDto;
import com.kondr.profile.mapper.ProfileMapper;
import com.kondr.profile.persistence.service.ProfileDocumentService;
import com.kondr.shared.constant.ApiParams;
import com.kondr.shared.constant.ApiPaths;
import com.kondr.shared.dto.profile.PublicProfileResponseDto;
import com.kondr.shared.exception.InvalidRequestException;
import com.kondr.shared.utils.AppStringUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(ApiPaths.PROFILES)
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileDocumentService profileDocumentService;

  private final ProfileMapper profileMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<FullProfileResponseDto> createUser(@RequestBody @Valid ProfileRequestDto profileDto) {
    return profileDocumentService.create(profileDto)
        .map(profileMapper::toFullDto);
  }

  @GetMapping(ApiPaths.ME)
  public Mono<ResponseEntity<FullProfileResponseDto>> getCurrentUser() {
    return profileDocumentService.findCurrentProfile()
        .map(profileMapper::toFullDto)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping
  public Flux<PublicProfileResponseDto> getProfiles(@RequestParam(ApiParams.IDS) List<String> ids) {
    log.info("Searching profiles by IDs {}", ids);

    return profileDocumentService.findByIds(ids)
        .map(profileMapper::toPublicDto);
  }

  @GetMapping(ApiPaths.SEARCH)
  public Flux<PublicProfileResponseDto> findProfiles(
      @RequestParam(name = "username", required = false) String username,
      @RequestParam(name = "name-tokens", required = false) List<String> nameTokens) {

    log.info("Search request: username={}, nameTokens={}", username, nameTokens);

    return StringUtils.isBlank(username) && AppStringUtils.isAllBlank(nameTokens)
        ? Flux.error(new InvalidRequestException(ErrorMessages.MISSING_SEARCH_PARAMS))
        : profileDocumentService.findProfiles(username, nameTokens)
            .map(profileMapper::toPublicDto);
  }

}