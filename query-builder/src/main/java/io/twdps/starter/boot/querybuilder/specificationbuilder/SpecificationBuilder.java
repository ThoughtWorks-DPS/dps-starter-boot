package io.twdps.starter.boot.querybuilder.specificationbuilder;

import io.twdps.starter.boot.querybuilder.model.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface SpecificationBuilder<T> {

  /**
   * Parses search criteria into array of SearchCriteria.
   *
   * @param search String
   * @return List<SearchCriteria>
   */
  List<SearchCriteria> parse(String search);
  /**
   * Builds Specification object for entity T optionally based on the array of search criteria.
   *
   * @param params List of search criteria
   * @return Optional<Specification<T>>
   */
  Optional<Specification<T>> build(List<SearchCriteria> params);
  /**
   * Builds Specification object for entity T optionally based on the search string validity.
   *
   * @param search String
   * @return Optional<Specification<T>>
   */
  Optional<Specification<T>> parseAndBuild(String search);
}
