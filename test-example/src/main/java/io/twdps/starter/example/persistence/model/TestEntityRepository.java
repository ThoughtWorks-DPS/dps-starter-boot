package io.twdps.starter.example.persistence.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TestEntityRepository extends CrudRepository<TestEntity, String> {

  Optional<TestEntity> findByUserName(String userName);
}
