package io.twdps.starter.boot.querybuilder.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class SearchCriteriaTest {

  @Test
  void testSearchCriteriaDoesNotBuildNestedSearchCriteriaValueWhenSimpleSearch() {
    SearchCriteria searchCriteria = new SearchCriteria(false, "id", SearchOperation.EQUALITY, "1");
    assertThat(searchCriteria.getValue()).isEqualTo("1");
  }

  @Test
  @Disabled
  void testSearchCriteriaBuildNestedSearchCriteriaValueWhenNestedSearch() {
    SearchCriteria searchCriteria = new SearchCriteria(false, "", SearchOperation.EQUALITY, "id=1");
    assertThat(searchCriteria.getKey()).isEqualTo("id");
    assertThat(searchCriteria.getValue()).isEqualTo("1");
    assertThat(searchCriteria.getOperation()).isNull();
    assertThat(searchCriteria.isOrPredicate()).isFalse();
  }
}
