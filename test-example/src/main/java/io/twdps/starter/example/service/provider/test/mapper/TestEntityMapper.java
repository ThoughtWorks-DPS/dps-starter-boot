package io.twdps.starter.example.service.provider.test.mapper;

import io.twdps.starter.example.persistence.model.TestEntity;
import io.twdps.starter.example.service.spi.test.model.TestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TestEntityMapper {

  TestEntity toEntity(TestModel src);

  default Optional<TestEntity> toEntity(Optional<TestModel> src) {
    return Optional.ofNullable(toEntity(src.orElse(null)));
  }

  List<TestEntity> toEntityList(List<TestModel> src);

  TestModel toModel(TestEntity src);

  default Optional<TestModel> toModel(Optional<TestEntity> src) {
    return Optional.ofNullable(toModel(src.orElse(null)));
  }

  List<TestModel> toModelList(Iterable<TestEntity> src);

  @Mapping(target = "id", ignore = true)
  TestEntity updateMetadata(TestModel src, @MappingTarget TestEntity dst);

}
