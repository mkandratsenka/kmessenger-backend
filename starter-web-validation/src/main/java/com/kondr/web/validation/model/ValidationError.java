package com.kondr.web.validation.model;

import java.util.List;

/**
 * Модель, представляющая ошибки валидации для указанного поля.
 *
 * @param field имя поля, в котором произошли ошибки.
 * @param messages список сообщений об ошибках.
 */
public record ValidationError(String field, List<String> messages) {

}