package com.kondr.conversation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

/**
 * DTO для ответа на запрос фиксации порядкового номера прочитанных сообщений.
 *
 * @param lastReadDate обновленная дата и время в случае успешной фиксации номера.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReadSeqCommitResponseDto(

    Instant lastReadDate) {

  private static final ReadSeqCommitResponseDto EMPTY = new ReadSeqCommitResponseDto(null);

  public static ReadSeqCommitResponseDto empty() {
    return EMPTY;
  }

}
