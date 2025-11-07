package com.kondr.conversation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для начитки пачки сообщений на основе последовательности и количества.
 *
 * @param fromExclusiveSeq номер сообщения, с которого начинается начитка (исключительно).
 * @param count            указывает направление начитки и количество начитываемых сообщений.
 */
public record MessageBatchRequestDto(

    @NotNull
    @Min(0)
    Long fromExclusiveSeq,

    @NotNull
    @Min(-200)  // отрицательное — загрузка до fromExclusiveSeq
    @Max(200)   // положительное — после fromExclusiveSeq
    Integer count) {

}