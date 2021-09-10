package io.twdps.starter.boot.querybuilder.misc;

import io.twdps.starter.boot.querybuilder.model.SearchOperation;
import io.twdps.starter.boot.querybuilder.model.SearchSection;

import java.util.Optional;

public final class SpecificationUtils {

  public static Optional<String> determineSplitOperation(String search) {
    if (search.contains(Constants.COMMA)) {
      return Optional.of(Constants.COMMA);
    }
    if (search.contains(Constants.OR_PREDICATE_FLAG)) {
      return Optional.of(Constants.OR_PREDICATE_FLAG);
    }
    return Optional.empty();
  }

  public static String[] splitSearchOperations(String search, String operator) {
    if (operator == null) {
      return new String[] {search};
    }
    return search.split(operator);
  }

  public static boolean isValueNullKey(String search) {
    return search.equals("null");
  }

  public static SearchOperation resolveSearchOperation(SearchSection searchSection) {
    // TODO: I don't think this function is necessary since we should be able to detect
    // the search function directly
    var parsedSearchOperation = SearchOperation.of(searchSection.getOperation());
    if (parsedSearchOperation == SearchOperation.EQUALITY) {
      boolean startWithAsterisk = searchSection.getPrefix().contains(Constants.ASTERISK);
      boolean endWithAsterisk = searchSection.getSuffix().contains(Constants.ASTERISK);
      if (startWithAsterisk && endWithAsterisk) return SearchOperation.CONTAINS;
      if (!startWithAsterisk && !endWithAsterisk) return SearchOperation.EQUALITY;
      if (startWithAsterisk) return SearchOperation.ENDS_WITH;
      return SearchOperation.STARTS_WITH;
    }
    return parsedSearchOperation;
  }

  // Not necessary
  @Deprecated
  public static boolean isKeyCompound(String key) {
    return key.contains(Constants.DOT);
  }

  // Not necessary
  @Deprecated
  public static String[] getCompoundKeys(String key) {
    return key.split(Constants.DOT_REGEX, 2);
  }

  private SpecificationUtils() {}
}
