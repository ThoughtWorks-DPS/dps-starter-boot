package io.twdps.starter.boot.querybuilder.model;

import lombok.Getter;

@Getter
public class SearchCriteria {

  private final String key;
  private final SearchOperation operation;
  private Object value;
  private final boolean orPredicate;

  public SearchCriteria(
      final boolean isOrPredicate,
      final String key,
      final SearchOperation operation,
      final Object value) {
    this.key = key;
    this.operation = operation;
    this.value = value;
    this.orPredicate = isOrPredicate;
  }
}
