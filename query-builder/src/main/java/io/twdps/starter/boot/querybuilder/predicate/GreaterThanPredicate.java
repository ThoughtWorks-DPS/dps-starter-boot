package io.twdps.starter.boot.querybuilder.predicate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GreaterThanPredicate<T> extends BasePredicate<T> {

  public GreaterThanPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
    super(objectMapper, idClazz);
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    return builder.greaterThan(
        getCriteriaExpressionKey(root), parseValue(getCriteriaObjectValue().toString(), builder));
  }
}
