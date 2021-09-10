package io.twdps.starter.boot.querybuilder.predicate;

import static io.twdps.starter.boot.querybuilder.misc.Constants.PERCENT_SIGN;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class StartsWithPredicate<T> extends BasePredicate<T> {

  public StartsWithPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
    super(objectMapper, idClazz);
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    return builder.like(
        builder.upper(getCriteriaStringExpressionKey(root).as(String.class)),
        String.join("", getCriteriaObjectValue().toString().toUpperCase(), PERCENT_SIGN));
  }
}
