package io.twdps.starter.boot.querybuilder.misc;

import static io.twdps.starter.boot.querybuilder.misc.Constants.ASTERISK;
import static io.twdps.starter.boot.querybuilder.misc.Constants.COLON;
import static io.twdps.starter.boot.querybuilder.misc.Constants.COMMA;
import static io.twdps.starter.boot.querybuilder.misc.Constants.OR_PREDICATE_FLAG;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.CONTAINS;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.ENDS_WITH;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.EQUALITY;
import static io.twdps.starter.boot.querybuilder.model.SearchOperation.STARTS_WITH;
import static org.assertj.core.api.Assertions.assertThat;

import io.twdps.starter.boot.querybuilder.model.SearchOperation;
import io.twdps.starter.boot.querybuilder.model.SearchSection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
class SpecificationUtilsTest {

  @Test
  void testDetermineSplitOperationReturnsEmptyWhenInvalidOperator() {
    Optional<String> result = SpecificationUtils.determineSplitOperation("name:*test*|id:1");
    assertThat(result).isNotPresent();
  }

  @Test
  void testDetermineSplitOperationReturnsComa() {
    Optional<String> result = SpecificationUtils.determineSplitOperation("name:*test*,id:1");
    assertThat(result).contains(COMMA);
  }

  @Test
  void testDetermineSplitOperationReturnsOrPredicateFlag() {
    Optional<String> result = SpecificationUtils.determineSplitOperation("name:*test*'id:1");
    assertThat(result).contains(OR_PREDICATE_FLAG);
  }

  @Test
  void testSplitSearchOperationsReturnsSingleSearchWhenNullOperator() {
    String[] result = SpecificationUtils.splitSearchOperations("name:*test*", null);
    assertThat(result).hasSize(1);
  }

  @Test
  void testSplitSearchOperationsReturnsSingleSearchWhenOperatorButNotConcatenatedSearches() {
    String[] result = SpecificationUtils.splitSearchOperations("name:*test*", COMMA);
    assertThat(result).hasSize(1);
  }

  @Test
  void testSplitSearchOperationsReturnsMultipleSearchesWhenOperatorAndConcatenatedSearches() {
    String[] result = SpecificationUtils.splitSearchOperations("name:*test*,id:1", COMMA);
    assertThat(result).hasSize(2);
  }

  @Test
  void testResolveSearchOperationReturnsContainsWhenColonAndStartEndAsterisks() {
    SearchSection searchSection = new SearchSection("key", COLON, null, ASTERISK, ASTERISK, false);
    SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
    assertThat(result).isEqualTo(CONTAINS);
  }

  @Test
  void testResolveSearchOperationReturnsEqualityWhenColonButNotStartNorEndAsterisks() {
    SearchSection searchSection = new SearchSection("key", COLON, null, "", "", false);
    SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
    assertThat(result).isEqualTo(EQUALITY);
  }

  @Test
  void testResolveSearchOperationReturnsEndsWithWhenColonAndStartAsterisk() {
    SearchSection searchSection = new SearchSection("key", COLON, null, ASTERISK, "", false);
    SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
    assertThat(result).isEqualTo(ENDS_WITH);
  }

  @Test
  void testResolveSearchOperationReturnsEndsWithWhenColonAndEndAsterisk() {
    SearchSection searchSection = new SearchSection("key", COLON, null, "", ASTERISK, false);
    SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
    assertThat(result).isEqualTo(STARTS_WITH);
  }

  @Test
  void testIsKeyCompoundReturnsSuccessfully() {
    assertThat(SpecificationUtils.isKeyCompound("list.nested")).isTrue();
  }

  @Test
  void testGetCompoundKeysReturnsSuccessfully() {
    String[] result = SpecificationUtils.getCompoundKeys("list.nested1");
    assertThat(result).hasSize(2);
    assertThat(result[0]).isEqualTo("list");
    assertThat(result[1]).isEqualTo("nested1");
  }
}
