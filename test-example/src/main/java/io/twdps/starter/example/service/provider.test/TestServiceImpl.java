package io.twdps.starter.example.service.provider.test;

import io.twdps.starter.example.persistence.model.TestEntityRepository;
import io.twdps.starter.example.service.provider.test.mapper.TestEntityMapper;
import io.twdps.starter.example.service.spi.test.TestService;
import io.twdps.starter.example.service.spi.test.model.TestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TestServiceImpl implements TestService {

  private TestEntityRepository repository;
  private TestEntityMapper mapper;

  TestServiceImpl(TestEntityRepository repository, TestEntityMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /**
   * add a new TestModel entity.
   *
   * @param resource resource info to add (id should be null)
   * @return new resource object with valid id
   */
  public TestModel add(TestModel resource) {
    TestModel saved = mapper.toModel(repository.save(mapper.toEntity(resource)));
    return saved;
  }

  /**
   * find resource by user name.
   *
   * @param userName username criteria to match
   * @return matching record, or null
   */
  public Optional<TestModel> findByUserName(String userName) {
    log.info("looking up by username:{}", userName);
    Optional<TestModel> resource = mapper.toModel(repository.findByUserName(userName));
    return resource;
  }

  @Override
  public Optional<TestModel> findById(String id) {
    Optional<TestModel> resource = mapper.toModel(repository.findById(id));
    return resource;
  }

  @Override
  public List<TestModel> findAll() {
    List<TestModel> resource = mapper.toModelList(repository.findAll());
    return resource;
  }

  @Override
  public Optional<TestModel> updateById(String id, TestModel record) {
    Optional<TestModel> resource =
        mapper.toModel(
            repository
                .findById(id)
                .map((obj) -> mapper.updateMetadata(record, obj))
                .map((obj) -> repository.save(obj)));

    return resource;
  }

  @Override
  public Optional<TestModel> deleteById(String id) {
    Optional<TestModel> resource = findById(id);
    repository.deleteById(id);
    return resource;
  }
}
