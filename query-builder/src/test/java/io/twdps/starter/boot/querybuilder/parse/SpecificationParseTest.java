package io.twdps.starter.boot.querybuilder.parse;

import static io.twdps.starter.boot.querybuilder.model.SearchOperation.OPERATION_EXPRESSION;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.REGEX_PREFIX;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.REGEX_SIMPLE_SUFFIX;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.model.SearchCriteria;
import io.twdps.starter.boot.querybuilder.predicate.CollectionContainsPredicate;
import io.twdps.starter.boot.querybuilder.predicate.ContainsPredicate;
import io.twdps.starter.boot.querybuilder.predicate.EndsWithPredicate;
import io.twdps.starter.boot.querybuilder.predicate.GreaterThanOrEqualPredicate;
import io.twdps.starter.boot.querybuilder.predicate.GreaterThanPredicate;
import io.twdps.starter.boot.querybuilder.predicate.LessThanOrEqualPredicate;
import io.twdps.starter.boot.querybuilder.predicate.LessThanPredicate;
import io.twdps.starter.boot.querybuilder.predicate.LikePredicate;
import io.twdps.starter.boot.querybuilder.predicate.NegationPredicate;
import io.twdps.starter.boot.querybuilder.predicate.StartsWithPredicate;
import io.twdps.starter.boot.querybuilder.specificationbuilder.SpecificationBuilderImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)
class SpecificationParseTest {

  private Pattern pattern;

  private static final String SIMPLE_REGEX_PATTERN =
      String.join("", REGEX_PREFIX, OPERATION_EXPRESSION, REGEX_SIMPLE_SUFFIX);

  // TODO: Remove this
  private final SpecificationBuilderImpl<Object> specificationBuilder =
      new SpecificationBuilderImpl<>(new ObjectMapper(), Object.class);

  public SpecificationParseTest() {
    pattern = Pattern.compile(SIMPLE_REGEX_PATTERN);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "asdasdasda", " "})
  void testNoPatternMatch(String search) {
    Matcher matcher = pattern.matcher(search);
    assertThat(matcher.matches()).isFalse();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "id:1",
        //      "nested:{id=1}",
        "list.id:1",
        //      "list.nested:{id=1}",
      })
  void testPatternMatches(String search) {
    Matcher matcher = pattern.matcher(search);
    assertThat(matcher.matches()).isTrue();
    assertThat(matcher.group(2)).isEqualTo(":");
    assertThat(matcher.group(3)).isEqualTo("1");
  }

  @Disabled("Used for testing regex patterns")
  @Test
  void testRegexPattern() {
    assertThat(SIMPLE_REGEX_PATTERN).isEqualTo("guaranteed failure");
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "collection-textValue",
        //        "nested-{collection=textValue}",
        //        "list.nested-{collection=textValue}"
      })
  void testParseAndBuildReturnsValidCollectionContainsSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(CollectionContainsPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "name:*search*",
        //        "nested:{name=*search*}",
        "list.name:*search*",
        //        "list.nested:{name=*search*}"
      })
  void testParseAndBuildReturnsValidContainsSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(ContainsPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "name:*search",
        //        "nested:{name=*search}",
        "list.name:*search",
        //        "list.nested:{name=*search}"
      })
  void testParseAndBuildReturnsValidEndsWithSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(EndsWithPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "name:search*",
        //        "nested:{name=search*}",
        "list.name:search*",
        //        "list.nested:{name=search*}"
      })
  void testParseAndBuildReturnsValidStartsWithSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(StartsWithPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "id>1",
        //      "nested>{id=1}",
        "list.id>1",
        //      "list.nested>{id=1}"
      })
  void testParseAndBuildReturnsValidGreaterThanSpecificationWhenValidNumericalSearch(
      String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(GreaterThanPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "id<1",
        //      "nested<{id=1}",
        "list.id<1",
        //      "list.nested<{id=1}"
      })
  void testParseAndBuildReturnsValidLessThanSpecificationWhenValidNumericalSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(LessThanPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "id>:1",
        //      "nested>:{id=1}",
        "list.id>:1",
        //      "list.nested>:{id=1}"
      })
  void testParseAndBuildReturnsValidGreaterThanOrEqualSpecificationWhenValidNumericalSearch(
      String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(GreaterThanOrEqualPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "id<:1",
        //      "nested<:{id=1}",
        "list.id<:1",
        //      "list.nested<:{id=1}"
      })
  void testParseAndBuildReturnsValidLessThanOrEqualSpecificationWhenValidNumericalSearch(
      String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(LessThanOrEqualPredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "name~textName",
        //        "nested~{name=textName}",
        "list.name~textName",
        //        "list.nested~{name=textName}"
      })
  void testParseAndBuildReturnsValidLikeSpecificationWhenValidStringSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(LikePredicate.class);
  }

  @Disabled
  @ParameterizedTest
  @ValueSource(
      strings = {
        "name!notThisName",
        //        "nested!{name=notThisName}",
        "list.name!notThisName",
        //        "list.nested!{name=notThisName}"
      })
  void testParseAndBuildReturnsValidNegationSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(NegationPredicate.class);
  }

  @Disabled
  @Test
  void testParseAndBuildReturnsCombinedSpecificationWhenMultipleANDSearches() {
    Optional<Specification<Object>> result =
        specificationBuilder.parseAndBuild("name~textName,id<1");
    assertThat(result).isPresent();
  }

  @Disabled
  @Test
  void testParseAndBuildReturnsCombinedSpecificationWhenMultipleORSearches() {
    Optional<Specification<Object>> result =
        specificationBuilder.parseAndBuild("name~textName'id<1");
    assertThat(result).isPresent();
  }
}
