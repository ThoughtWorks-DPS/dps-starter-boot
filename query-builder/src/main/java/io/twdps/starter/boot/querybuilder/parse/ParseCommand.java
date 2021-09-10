package io.twdps.starter.boot.querybuilder.parse;

import io.twdps.starter.boot.querybuilder.model.SearchCriteria;

import java.util.List;

public interface ParseCommand {

  /**
   * Parses and constructs a List of SearchCriteria objects.
   *
   * @param search String[]
   * @param isOrPredicate boolean
   * @return List<SearchCriteria>
   */
  List<SearchCriteria> parse(String[] search, boolean isOrPredicate);
}
