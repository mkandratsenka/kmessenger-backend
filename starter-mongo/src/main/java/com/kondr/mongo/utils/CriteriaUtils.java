package com.kondr.mongo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Класс, содержащий набор утилитных методов для создания критериев запроса MongoDB.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CriteriaUtils {

  public static Criteria startsWithIgnoreCase(String field, String value) {
    return StringUtils.isBlank(value)
        ? null
        : Criteria.where(field).regex("^" + value.toLowerCase());
  }

  public static Criteria notEqualTo(String field, String value) {
    return StringUtils.isBlank(value)
        ? null
        : Criteria.where(field).ne(value);
  }

}