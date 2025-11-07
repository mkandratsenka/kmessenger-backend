package com.kondr.conversation.dto.response;

import lombok.Builder;

/**
 * DTO, содержащее информацию о собеседнике.
 *
 * @param id        ID собеседника.
 * @param username  имя пользователя (никнейм).
 * @param lastName  фамилия собеседника.
 * @param firstName имя собеседника.
 */
@Builder
public record InterlocutorPreviewDto(

    String id,
    String username,
    String lastName,
    String firstName) {

}