package io.twdps.starter.boot.querybuilder.model;

import static io.twdps.starter.boot.querybuilder.misc.Constants.*;
import static io.twdps.starter.boot.querybuilder.misc.Constants.CLOSING_EXCLAMATION;
import static io.twdps.starter.boot.querybuilder.misc.Constants.COLON;
import static io.twdps.starter.boot.querybuilder.misc.Constants.DASH;
import static io.twdps.starter.boot.querybuilder.misc.Constants.GREATER_THAN_SIGN;
import static io.twdps.starter.boot.querybuilder.misc.Constants.LESS_THAN_SIGN;
import static io.twdps.starter.boot.querybuilder.misc.Constants.TILDE;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public enum SearchOperation {

  // ... enum values
  EQUALITY(COLON),
  INEQUALITY(NE_DIGRAPH),
  GREATER_THAN_OR_EQUAL(GTE_DIGRAPH),
  GREATER_THAN(GREATER_THAN_SIGN),
  LESS_THAN_OR_EQUAL(LTE_DIGRAPH),
  LESS_THAN(LESS_THAN_SIGN),
  LIKE(TILDE),
  UNLIKE(UNLIKE_DIGRAPH),
  CONTAINS(PREFIX_SUFFIX_DIGRAPH),
  STARTS_WITH(PREFIX_DIGRAPH),
  ENDS_WITH(SUFFIX_DIGRAPH),
  NOT_CONTAINS(NOT_PREFIX_SUFFIX_DIGRAPH),
  NOT_STARTS_WITH(NOT_PREFIX_DIGRAPH),
  NOT_ENDS_WITH(NOT_SUFFIX_DIGRAPH),
  COLLECTION_CONTAINS(DASH),
  NEGATION(CLOSING_EXCLAMATION);

  public final String label;

  private static final Map<String, SearchOperation> BY_LABEL =
      Arrays.stream(values()).collect(Collectors.toMap((v) -> v.label, (v) -> v));
  // = {COLON, CLOSING_EXCLAMATION, GREATER_THAN_SIGN, LESS_THAN_SIGN, TILDE, DASH};
  private static final String[] SIMPLE_OPERATION_SET =
      Arrays.stream(values())
          .map((v) -> v.label)
          .sorted(Comparator.reverseOrder()) // ensure more specific operators are found first
          .collect(Collectors.toList())
          .toArray(new String[0]);
  private static final String[] SIMPLE_REGEX_SET =
      Arrays.stream(SIMPLE_OPERATION_SET)
          .map((s) -> s.replaceAll("\\*", java.util.regex.Matcher.quoteReplacement("\\*")))
          .collect(Collectors.toList())
          .toArray(new String[0]);
  public static final String OPERATION_EXPRESSION =
      String.join("|", SearchOperation.SIMPLE_REGEX_SET);
  public static final String REGEX_PREFIX = "([.\\w]+?)(";
  public static final String REGEX_SIMPLE_SUFFIX = ")([^`,]+)";
  public static final String REGEX_NESTED_SUFFIX =
      ")(\\{)([.\\w]+?)(=)(\\p{Punct}?)([-\\p{L}\\p{Digit}\\/\\p{Space}:\\(]+?)(\\p{Punct}?)(})";

  SearchOperation(String label) {
    this.label = label;
  }

  //  static {
  //    for (SearchOperation e : values()) {
  //      BY_LABEL.put(e.label, e);
  //    }
  //  }

  public static SearchOperation of(String label) {
    return BY_LABEL.get(label);
  }
}
