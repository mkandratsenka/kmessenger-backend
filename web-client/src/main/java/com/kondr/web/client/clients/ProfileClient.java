package com.kondr.web.client.clients;

import com.kondr.shared.dto.profile.PublicProfileResponseDto;
import java.util.List;
import reactor.core.publisher.Flux;

/**
 * Клиент для взаимодействия с сервисом профилей.
 */
public interface ProfileClient {

  /**
   * Возвращает поток публичных профилей по списку ID.
   *
   * @param idList список ID пользователей.
   * @return {@link Flux} с найденными публичными профилями.
   */
  Flux<PublicProfileResponseDto> getProfiles(List<String> idList);

}