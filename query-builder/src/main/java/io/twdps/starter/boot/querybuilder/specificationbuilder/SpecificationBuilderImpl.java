package io.twdps.starter.boot.querybuilder.specificationbuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.querybuilder.misc.Constants;
import io.twdps.starter.boot.querybuilder.misc.SpecificationUtils;
import io.twdps.starter.boot.querybuilder.model.SearchCriteria;
import io.twdps.starter.boot.querybuilder.parse.*;
import io.twdps.starter.boot.querybuilder.predicate.SpecificationFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class SpecificationBuilderImpl<T> implements SpecificationBuilder<T> {

  private final ObjectMapper objectMapper;
  private final Class<? extends Serializable> idClazz;
  private static final List<ParseCommand> PARSE_COMMANDS =
      Arrays.asList(
          new SimpleParseCommand() // ,
          //            new NestedParseCommand(),
          //            new ReferencedSimpleParseCommand(),
          //            new ReferencedCompoundParseCommand()
          );

  public SpecificationBuilderImpl(ObjectMapper objectMapper, Class<T> entityClazz) {
    this.objectMapper = objectMapper;
    this.idClazz = initializeIdClazz(entityClazz);
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Serializable> initializeIdClazz(Class<T> entityClazz) {
    try {
      return ((Class<? extends Serializable>)
          ((ParameterizedType) entityClazz.getGenericSuperclass()).getActualTypeArguments()[0]);
    } catch (Exception e) {
      log.warn("Could not determine entity generic id type, error: {}", e.getMessage());
    }
    return Serializable.class;
  }

  @Override
  public List<SearchCriteria> parse(String search) {
    if ((null == search) || (search.isBlank())) {
      return List.of();
    }
    Optional<String> splitOperation = SpecificationUtils.determineSplitOperation(search);
    boolean isOrPredicate =
        splitOperation.isPresent() ? splitOperation.equals(Constants.OR_PREDICATE_FLAG) : false;
    String[] searchQueries =
        SpecificationUtils.splitSearchOperations(search, splitOperation.orElse(null));
    return PARSE_COMMANDS.stream()
        .map(p -> p.parse(searchQueries, isOrPredicate))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Specification<T>> parseAndBuild(String search) {
    return build(parse(search));
  }

  @Override
  public Optional<Specification<T>> build(List<SearchCriteria> params) {
    if (params.isEmpty()) {
      return Optional.empty();
    }
    var specFactory = new SpecificationFactory<T>(objectMapper, idClazz);
    Specification<T> result = specFactory.getSpecification(params.get(0));
    for (var i = 1; i < params.size(); i++)
      result =
          params.get(i).isOrPredicate()
              ? Objects.requireNonNull(Specification.where(result))
                  .or(specFactory.getSpecification(params.get(i)))
              : Objects.requireNonNull(Specification.where(result))
                  .and(specFactory.getSpecification(params.get(i)));
    return Optional.of(result);
  }

  protected Class<? extends Serializable> getIdClazz() {
    return this.idClazz;
  }
}
