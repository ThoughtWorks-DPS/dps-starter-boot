package io.twdps.starter.boot.querybuilder.parse;

import static io.twdps.starter.boot.querybuilder.model.SearchOperation.OPERATION_EXPRESSION;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.REGEX_PREFIX;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.REGEX_SIMPLE_SUFFIX;

import io.twdps.starter.boot.querybuilder.model.SearchSection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleParseCommand extends BaseParseCommand {

  private static final String SIMPLE_REGEX_PATTERN =
      String.join("", REGEX_PREFIX, OPERATION_EXPRESSION, REGEX_SIMPLE_SUFFIX);

  public SimpleParseCommand() {
    super(Pattern.compile(SIMPLE_REGEX_PATTERN));
  }

  @Override
  protected SearchSection process(Matcher matcher) {
    return new SearchSection(matcher.group(1), matcher.group(2), matcher.group(3), "", "");
  }
}
