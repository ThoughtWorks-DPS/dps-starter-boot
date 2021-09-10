package io.twdps.starter.boot.querybuilder.predicate;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.model.SearchCriteria;
import io.twdps.starter.boot.querybuilder.model.SearchOperation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class NegationPredicateTest {

  @Mock private Root<Object> root;

  @Mock private Join<Object, Object> joinPath;

  @Mock private CriteriaQuery<?> criteriaQuery;

  @Mock private CriteriaBuilder criteriaBuilder;

  private final NegationPredicate<Object> negationPredicate =
      new NegationPredicate<>(new ObjectMapper(), Long.class);

  @Test
  void testToPredicateWhenSingleSearchCriteriaValue() {
    SearchCriteria searchCriteria = new SearchCriteria(false, "id", SearchOperation.NEGATION, "1");
    negationPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
  }

  @Test
  void testToPredicateWhenSingleSearchCriteriaValueNullableSearch() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "id", SearchOperation.NEGATION, "null");
    negationPredicate.setSearchCriteria(searchCriteria);

    when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();

    negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).isNotNull(joinPath);
  }

  @Test
  @Disabled
  void testToPredicateWhenNestedSearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "nested", SearchOperation.NEGATION, "id=1");
    negationPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
    when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
  }

  @Test
  void testToPredicateWhenOneToManySearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "list.nested", SearchOperation.NEGATION, "1");
    negationPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join("list")).thenReturn(joinPath);
    when(joinPath.get("nested")).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
  }

  @Test
  @Disabled
  void testToPredicateWhenNestedOneToManySearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "list.nested", SearchOperation.NEGATION, "id=1");
    negationPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join("list")).thenReturn(joinPath);
    when(joinPath.join("nested")).thenReturn(joinPath);
    when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
  }
}
