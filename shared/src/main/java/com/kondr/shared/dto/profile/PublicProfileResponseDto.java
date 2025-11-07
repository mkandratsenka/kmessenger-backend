package com.kondr.shared.dto.profile;

/**
 * DTO для публичного профиля пользователя.
 *
 * @param id        ID пользователя.
 * @param username  имя пользователя (никнейм).
 * @param lastName  фамилия.
 * @param firstName имя.
 */
public record PublicProfileResponseDto(

    String id,
    String username,
    String lastName,
    String firstName) {

}