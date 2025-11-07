package com.kondr.profile.dto;

/**
 * DTO для публичного профиля пользователя.
 *
 * @param id        ID пользователя.
 * @param email     адрес электронной почты пользователя.
 * @param username  имя пользователя (никнейм).
 * @param lastName  фамилия.
 * @param firstName имя.
 */
public record FullProfileResponseDto(

    String id,
    String email,
    String username,
    String lastName,
    String firstName) {

}