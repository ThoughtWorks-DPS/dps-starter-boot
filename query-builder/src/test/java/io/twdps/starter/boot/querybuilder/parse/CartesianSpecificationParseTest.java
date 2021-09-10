package io.twdps.starter.boot.querybuilder.parse;

import static io.twdps.starter.boot.querybuilder.model.SearchOperation.OPERATION_EXPRESSION;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.REGEX_PREFIX;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.REGEX_SIMPLE_SUFFIX;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.CartesianProductTest;
import org.junitpioneer.jupiter.CartesianValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)
class CartesianSpecificationParseTest {

  private Pattern pattern;

  private static final String SIMPLE_REGEX_PATTERN =
      String.join("", REGEX_PREFIX, OPERATION_EXPRESSION, REGEX_SIMPLE_SUFFIX);

  public CartesianSpecificationParseTest() {
    pattern = Pattern.compile(SIMPLE_REGEX_PATTERN);
  }

  @CartesianProductTest
  @DisplayName("Key x Op x Value")
  @CartesianValueSource(strings = {"id", "list.id", "list.some.id"})
  @CartesianValueSource(
      strings = {
        "!:", "!~", "!*~", "!~*", "!*~*", ">:", ">", "<:", "<", "*~", "~*", "*~*", "-", "~", ":"
      })
  //  @CartesianEnumSource(value=SearchOperation.class)
  @CartesianValueSource(strings = {"1", "~133", "ab.cd", "-$@*", "2021-05-05T05:05:05.005Z"})
  void testPatternMatches(String key, String op, String value) {
    String search = key + op + value;
    Matcher matcher = pattern.matcher(search);
    assertThat(matcher.matches()).isTrue();
    assertThat(matcher.group(1)).isEqualTo(key);
    assertThat(matcher.group(2)).isEqualTo(op);
    assertThat(matcher.group(3)).isEqualTo(value);
  }
}
