package io.twdps.starter.service.spi.foo;

import io.twdps.starter.model.foo.FooModel;

import java.util.List;
import java.util.Optional;

public interface FooService {

  FooModel add(FooModel account);

  List<FooModel> findByLastName(String lastName);

  Optional<FooModel> findByUserName(String userName);

  Optional<FooModel> findById(String id);

  List<FooModel> findAll();

  Optional<FooModel> updateById(String id, FooModel record);

  Optional<FooModel> deleteById(String id);
}
