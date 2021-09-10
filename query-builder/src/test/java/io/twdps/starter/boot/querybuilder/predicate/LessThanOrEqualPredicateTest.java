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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class LessThanOrEqualPredicateTest {

  @Mock private Root<Object> root;

  @Mock private Path path;

  @Mock private Join<Object, Object> joinPath;

  @Mock private CriteriaQuery<?> criteriaQuery;

  @Mock private CriteriaBuilder criteriaBuilder;

  private final LessThanOrEqualPredicate<Object> lessThanOrEqualPredicate =
      new LessThanOrEqualPredicate<>(new ObjectMapper(), Long.class);

  @Test
  void testToPredicateWhenSingleSearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "id", SearchOperation.LESS_THAN_OR_EQUAL, "1");
    lessThanOrEqualPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.get(searchCriteria.getKey())).thenReturn(path);
    doReturn(Integer.class).when(path).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    lessThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).lessThanOrEqualTo(path, integerExpression);
  }

  @Test
  @Disabled
  void testToPredicateWhenNestedSearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "nested", SearchOperation.LESS_THAN_OR_EQUAL, "id=1");
    lessThanOrEqualPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
    when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(path);
    doReturn(Integer.class).when(path).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    lessThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).lessThanOrEqualTo(path, integerExpression);
  }

  @Test
  void testToPredicateWhenOneToManySearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "list.nested", SearchOperation.LESS_THAN_OR_EQUAL, "1");
    lessThanOrEqualPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join("list")).thenReturn(joinPath);
    when(joinPath.get("nested")).thenReturn(path);
    doReturn(Integer.class).when(path).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    lessThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).lessThanOrEqualTo(path, integerExpression);
  }

  @Test
  @Disabled
  void testToPredicateWhenNestedOneToManySearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "list.nested", SearchOperation.LESS_THAN_OR_EQUAL, "id=1");
    lessThanOrEqualPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join("list")).thenReturn(joinPath);
    when(joinPath.join("nested")).thenReturn(joinPath);
    when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(path);
    doReturn(Integer.class).when(path).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    lessThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).lessThanOrEqualTo(path, integerExpression);
  }
}
