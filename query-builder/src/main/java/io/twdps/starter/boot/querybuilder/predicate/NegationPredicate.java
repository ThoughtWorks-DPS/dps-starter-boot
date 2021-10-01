package io.twdps.starter.boot.querybuilder.predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.misc.SpecificationUtils;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NegationPredicate<T> extends BasePredicate<T> {

  public NegationPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
    super(objectMapper, idClazz);
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    if (SpecificationUtils.isValueNullKey(getCriteriaObjectValue().toString()))
      return builder.isNotNull(getCriteriaStringExpressionKey(root));
    return builder.notEqual(
        getCriteriaStringExpressionKey(root),
        parseValue(getCriteriaObjectValue().toString(), builder));
  }
}
