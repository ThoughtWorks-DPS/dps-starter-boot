package io.twdps.starter.boot.querybuilder.specificationbuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.model.SearchCriteria;
import io.twdps.starter.boot.querybuilder.parse.SimpleParseCommand;
import io.twdps.starter.boot.querybuilder.predicate.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SpecificationBuilderImplTest {

  private final SpecificationBuilderImpl<Object> specificationBuilder =
      new SpecificationBuilderImpl<>(new ObjectMapper(), Object.class);

  @Test
  void testSpecificationBuilderImplIdClassGenerics() {
    SpecificationBuilderImpl<EntityGenericIdTestObject> specificationBuilder2 =
        new SpecificationBuilderImpl<>(new ObjectMapper(), EntityGenericIdTestObject.class);
    assertThat(specificationBuilder2.getIdClazz()).isEqualTo(Long.class);
    assertThat(specificationBuilder.getIdClazz()).isEqualTo(Serializable.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "asdasdasda", " "})
  void testParseAndBuildReturnsEmptyWhenInvalidSearch(String search) {
    Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
    assertThat(result).isNotPresent();
  }

  @ParameterizedTest
  @NullSource
  void testParseAndBuildReturnsEmptyWhenNullSearch(String search) {
    Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
    assertThat(result).isNotPresent();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "id:1",
        //      "nested:{id=1}",
        "list.id:1",
        //      "list.nested:{id=1}",
      })
  void testParseAndBuildReturnsValidEqualitySpecificationWhenValidSearch(String search) {
    SimpleParseCommand parser = new SimpleParseCommand();
    List<SearchCriteria> crit = parser.parse(new String[] {search}, false);
    assertThat(crit.size()).isGreaterThan(0);

    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(EqualityPredicate.class);
  }

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

  @ParameterizedTest
  @ValueSource(
      strings = {
        "name*~*search",
        //        "nested*~*{name=search}",
        "list.name*~*search",
        //        "list.nested*~*{name=search}"
      })
  void testParseAndBuildReturnsValidContainsSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(ContainsPredicate.class);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "name~*search",
        //        "nested~*{name=search}",
        "list.name~*search",
        //        "list.nested~*{name=search}"
      })
  void testParseAndBuildReturnsValidEndsWithSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(EndsWithPredicate.class);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "name*~search",
        //        "nested*~{name=search}",
        "list.name*~search",
        //        "list.nested*~{name=search}"
      })
  void testParseAndBuildReturnsValidStartsWithSpecificationWhenValidSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(StartsWithPredicate.class);
  }

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

  @ParameterizedTest
  @ValueSource(
      strings = {
        "name~textName",
        //        "nested~{name=textName}",
        "list.name~textName",
        //        "list.nested~{name=textName}"
        "name~textNameid_1"
      })
  void testParseAndBuildReturnsValidLikeSpecificationWhenValidStringSearch(String search) {
    List<SearchCriteria> params = specificationBuilder.parse(search);
    assertThat(params.size()).isGreaterThan(0);
    Optional<Specification<Object>> result = specificationBuilder.build(params);
    assertThat(result).isPresent();
    assertThat(result.get()).isInstanceOf(LikePredicate.class);
  }

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

  @Test
  void testParseAndBuildReturnsCombinedSpecificationWhenMultipleANDSearches() {
    Optional<Specification<Object>> result =
        specificationBuilder.parseAndBuild("name~textName,id<1");
    assertThat(result).isPresent();
  }

  @Test
  void testParseAndBuildReturnsCombinedSpecificationWhenMultipleORSearches() {
    Optional<Specification<Object>> result =
        specificationBuilder.parseAndBuild("name~textName'id<1");
    assertThat(result).isPresent();
  }
}
