package io.twdps.starter.example.controller.test;

import io.twdps.starter.boot.exception.RequestValidationException;
import io.twdps.starter.boot.exception.ResourceNotFoundException;
import io.twdps.starter.example.api.test.requests.TestRequest;
import io.twdps.starter.example.api.test.resources.TestResource;
import io.twdps.starter.example.api.test.responses.TestResponse;
import io.twdps.starter.example.service.spi.test.TestService;
import io.twdps.starter.example.service.spi.test.model.TestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class TestController implements TestResource {

  TestService manager;

  /**
   * constructor.
   *
   * @param manager instance of account manager
   */
  public TestController(TestService manager) {
    this.manager = manager;
  }

  @Override
  public ResponseEntity<TestResponse> addEntity(TestRequest request)
      throws RequestValidationException {
    TestModel resource = TestModel.builder().userName(request.getUserName()).build();
    TestModel saved = manager.add(resource);
    TestResponse response = new TestResponse(saved.getUserName());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<TestResponse> findEntityById(String id) throws ResourceNotFoundException {
    Optional<TestModel> resource = manager.findById(id);
    return new ResponseEntity<>(
        resource
            .map(r -> new TestResponse(r.getUserName()))
            .orElseThrow(() -> new ResourceNotFoundException(id)),
        HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<TestResponse> updateEntityById(String id, TestRequest request)
      throws ResourceNotFoundException, RequestValidationException {
    return new ResponseEntity<>(new TestResponse("foo"), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<TestResponse> deleteEntityById(String id) throws ResourceNotFoundException {
    return new ResponseEntity<>(new TestResponse("foo"), HttpStatus.OK);
  }
}
