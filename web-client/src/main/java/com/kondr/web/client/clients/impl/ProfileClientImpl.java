package com.kondr.web.client.clients.impl;

import com.kondr.web.client.clients.ProfileClient;
import com.kondr.shared.constant.ApiParams;
import com.kondr.shared.constant.ApiPaths;
import com.kondr.shared.dto.profile.PublicProfileResponseDto;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class ProfileClientImpl implements ProfileClient {

  private final WebClient webClient;

  public ProfileClientImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Flux<PublicProfileResponseDto> getProfiles(List<String> ids) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(ApiPaths.PROFILES)
            .queryParam(ApiParams.IDS, ids)
            .build()
        )
        .retrieve()
        .bodyToFlux(PublicProfileResponseDto.class);
  }

}