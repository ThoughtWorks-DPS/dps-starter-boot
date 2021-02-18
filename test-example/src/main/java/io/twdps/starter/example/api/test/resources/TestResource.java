package io.twdps.starter.example.api.test.resources;

import io.twdps.starter.boot.exception.RequestValidationException;
import io.twdps.starter.boot.exception.ResourceNotFoundException;
import io.twdps.starter.example.api.test.requests.TestRequest;
import io.twdps.starter.example.api.test.responses.TestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "/v1/example/test", produces = "application/json")
public interface TestResource {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity<TestResponse> addEntity(@RequestBody TestRequest request)
      throws RequestValidationException;

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<TestResponse> findEntityById(@PathVariable(value = "id") String id)
      throws ResourceNotFoundException;

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<TestResponse> updateEntityById(@PathVariable(value = "id") String id,
      @RequestBody TestRequest request)
      throws ResourceNotFoundException,  RequestValidationException;

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<TestResponse> deleteEntityById(@PathVariable(value = "id") String id)
      throws ResourceNotFoundException;

}
