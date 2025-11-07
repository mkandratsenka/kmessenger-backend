package com.kondr.mongo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Let.ExpressionVariable;
import org.springframework.util.Assert;

/**
 * Класс, содержащий набор утилитных методов для построения агрегаций MongoDB.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AggregationUtils {

  private static final String FIELD_CANNOT_BE_EMPTY = "Field cannot be empty";

  public static String fieldRef(String field) {
    Assert.hasText(field, FIELD_CANNOT_BE_EMPTY);
    return "$" + field;
  }

  public static String fieldPath(String alias, String field) {
    Assert.hasText(alias, "Alias cannot be empty");
    Assert.hasText(field, FIELD_CANNOT_BE_EMPTY);
    return alias + "." + field;
  }

  public static String variableRef(String variable) {
    Assert.hasText(variable, "Variable cannot be empty");
    return "$$" + variable;
  }

  public static ExpressionVariable variableForField(String variableName, String field) {
    Assert.hasText(field, FIELD_CANNOT_BE_EMPTY);
    return ExpressionVariable.newVariable(variableName).forField(fieldRef(field));
  }

  public static ComparisonOperators.Eq eqFieldToVar(String field, String variableName) {
    return ComparisonOperators.Eq.valueOf(fieldRef(field)).equalTo(variableRef(variableName));
  }

  public static ComparisonOperators.Eq eqFieldToVal(String field, String value) {
    return ComparisonOperators.Eq.valueOf(fieldRef(field)).equalToValue(value);
  }

}