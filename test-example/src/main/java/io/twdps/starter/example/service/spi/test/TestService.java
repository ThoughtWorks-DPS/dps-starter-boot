package io.twdps.starter.example.service.spi.test;

import io.twdps.starter.boot.exception.RequestValidationException;
import io.twdps.starter.example.service.spi.test.model.TestModel;

import java.util.List;
import java.util.Optional;

public interface TestService {

  TestModel add(TestModel resource) throws RequestValidationException;

  Optional<TestModel> findByUserName(String userName);

  Optional<TestModel> findById(String id);

  List<TestModel> findAll();

  Optional<TestModel> updateById(String id, TestModel record) throws RequestValidationException;

  Optional<TestModel> deleteById(String id);
}
