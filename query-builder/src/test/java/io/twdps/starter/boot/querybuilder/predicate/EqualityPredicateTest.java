package io.twdps.starter.boot.querybuilder.predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.model.SearchCriteria;
import io.twdps.starter.boot.querybuilder.model.SearchOperation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class EqualityPredicateTest {

  @Mock private Root<Object> root;

  @Mock private Join<Object, Object> joinPath;

  @Mock private CriteriaQuery<?> criteriaQuery;

  @Mock private CriteriaBuilder criteriaBuilder;

  private final EqualityPredicate<Object> equalityPredicate =
      new EqualityPredicate<>(new ObjectMapper(), Long.class);

  @Test
  void testToPredicateWhenSingleSearchCriteriaValue() {
    SearchCriteria searchCriteria = new SearchCriteria(false, "id", SearchOperation.EQUALITY, "1");
    equalityPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    equalityPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).equal(joinPath, integerExpression);
  }

  @Test
  void testToPredicateWhenSingleSearchCriteriaValueSerializable() {
    Serializable id = Long.valueOf("1");
    SearchCriteria searchCriteria = new SearchCriteria(false, "id", SearchOperation.EQUALITY, id);
    equalityPredicate.setSearchCriteria(searchCriteria);

    Expression<Long> longExpression = mock(Expression.class);

    when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
    doReturn(Serializable.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1L)).thenReturn(longExpression);

    equalityPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).equal(joinPath, longExpression);
  }

  @Test
  void testToPredicateWhenValueParsingException() throws IOException {
    Serializable id = Long.valueOf("1");
    SearchCriteria searchCriteria = new SearchCriteria(false, "id", SearchOperation.EQUALITY, id);

    ObjectMapper mockedObjectMapper = mock(ObjectMapper.class);
    byte[] mockedByteArray = new byte[0];

    when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
    doReturn(Serializable.class).when(joinPath).getJavaType();
    doReturn(mockedByteArray).when(mockedObjectMapper).writeValueAsBytes(any());
    doThrow(new JsonParseException(null, ""))
        .when(mockedObjectMapper)
        .readValue(mockedByteArray, Long.class);
    doThrow(new IllegalArgumentException("null literal")).when(criteriaBuilder).literal(null);

    EqualityPredicate<Object> equalityPredicate =
        new EqualityPredicate<>(mockedObjectMapper, Long.class);
    equalityPredicate.setSearchCriteria(searchCriteria);
    IllegalArgumentException exception =
        (IllegalArgumentException)
            catchThrowable(
                () -> equalityPredicate.toPredicate(root, criteriaQuery, criteriaBuilder));

    assertThat(exception.getMessage()).isEqualTo("null literal");
  }

  @Test
  void testToPredicateWhenSingleSearchCriteriaValueNullableSearch() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "id", SearchOperation.EQUALITY, "null");
    equalityPredicate.setSearchCriteria(searchCriteria);

    when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();

    equalityPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).isNull(joinPath);
  }

  @Test
  @Disabled
  void testToPredicateWhenNestedSearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "nested", SearchOperation.EQUALITY, "id=1");
    equalityPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
    when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    equalityPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).equal(joinPath, integerExpression);
  }

  @Test
  void testToPredicateWhenOneToManySearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "list.nested", SearchOperation.EQUALITY, "1");
    equalityPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join("list")).thenReturn(joinPath);
    when(joinPath.get("nested")).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    equalityPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).equal(joinPath, integerExpression);
  }

  @Test
  @Disabled
  void testToPredicateWhenNestedOneToManySearchCriteriaValue() {
    SearchCriteria searchCriteria =
        new SearchCriteria(false, "list.nested", SearchOperation.EQUALITY, "id=1");
    equalityPredicate.setSearchCriteria(searchCriteria);

    Expression<Integer> integerExpression = mock(Expression.class);

    when(root.join("list")).thenReturn(joinPath);
    when(joinPath.join("nested")).thenReturn(joinPath);
    when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
    doReturn(Integer.class).when(joinPath).getJavaType();
    when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

    equalityPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

    verify(criteriaBuilder, times(1)).equal(joinPath, integerExpression);
  }
}
