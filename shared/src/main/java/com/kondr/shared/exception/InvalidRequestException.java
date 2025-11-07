package com.kondr.shared.exception;

/**
 * Исключение, выбрасываемое при невалидных параметрах запроса.
 */
public class InvalidRequestException extends RuntimeException {

  /**
   * Создает исключение с заданным сообщением.
   *
   * @param message сообщение об ошибке.
   */
  public InvalidRequestException(String message) {
    super(message);
  }

}