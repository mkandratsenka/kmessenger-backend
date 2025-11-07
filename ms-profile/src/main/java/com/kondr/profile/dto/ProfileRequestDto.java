package com.kondr.profile.dto;

import com.kondr.profile.validation.constant.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO для запроса создания профиля пользователя.
 */
public record ProfileRequestDto(

    @NotBlank(message = "Введите имя пользователя")
    @Size(min = 3, max = 16, message = "Имя пользователя должно содержать от 3 до 16 символов")
    @Pattern(
        regexp = ValidationPatterns.USERNAME,
        message = "Имя пользователя может содержать только латинские буквы и цифры"
    )
    String username,

    @NotBlank(message = "Введите фамилию")
    @Size(min = 2, max = 24, message = "Имя пользователя должно содержать от 2 до 24 символов")
    @Pattern(
        regexp = ValidationPatterns.NAME,
        message = "Фамилия может содержать только буквы"
    )
    String lastName,

    @NotBlank(message = "Введите имя")
    @Size(min = 2, max = 24, message = "Имя пользователя должно содержать от 2 до 24 символов")
    @Pattern(
        regexp = ValidationPatterns.NAME,
        message = "Имя может содержать только буквы"
    )
    String firstName) {

}