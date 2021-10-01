package io.twdps.starter.boot.querybuilder.predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.misc.Constants;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotEndsWithPredicate<T> extends BasePredicate<T> {

  public NotEndsWithPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
    super(objectMapper, idClazz);
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    return builder.notLike(
        builder.upper(getCriteriaStringExpressionKey(root).as(String.class)),
        String.join("", Constants.PERCENT_SIGN, getCriteriaObjectValue().toString().toUpperCase()));
  }
}
