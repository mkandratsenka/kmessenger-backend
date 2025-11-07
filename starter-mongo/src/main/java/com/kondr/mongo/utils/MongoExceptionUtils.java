package com.kondr.mongo.utils;

import com.mongodb.MongoCommandException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Класс, содержащий набор утилитных методов для работы с исключениями MongoDB.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoExceptionUtils {

  /**
   * Код ошибки конфликта записи в MongoDB.
   */
  private static final int WRITE_CONFLICT_ERROR_CODE = 112;

  /**
   * Проверяет, является ли исключение конфликтом записи.
   *
   * @param throwable исключение для проверки.
   * @return {@code true}, если исключение является конфликтом записи.
   */
  public static boolean isWriteConflictException(Throwable throwable) {
    Throwable mongoThrowable = throwable instanceof DataIntegrityViolationException
        ? throwable.getCause()
        : throwable;

    return mongoThrowable instanceof MongoCommandException mongoCommandException
        && mongoCommandException.getErrorCode() == WRITE_CONFLICT_ERROR_CODE;
  }

}
