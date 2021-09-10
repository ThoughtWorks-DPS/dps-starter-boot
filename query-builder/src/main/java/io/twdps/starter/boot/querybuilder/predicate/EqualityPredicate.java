package io.twdps.starter.boot.querybuilder.predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.misc.SpecificationUtils;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EqualityPredicate<T> extends BasePredicate<T> {

  public EqualityPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
    super(objectMapper, idClazz);
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var searchValue = getCriteriaObjectValue().toString();
    if (SpecificationUtils.isValueNullKey(searchValue))
      return builder.isNull(getCriteriaStringExpressionKey(root));
    return builder.equal(getCriteriaStringExpressionKey(root), parseValue(searchValue, builder));
  }
}
