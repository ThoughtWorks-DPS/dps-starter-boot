package io.twdps.starter.boot.config;

import io.twdps.starter.boot.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apiguardian.api.API;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;

/**
 * AdviceTraits for handling exceptions during API calls.
 *
 * @see ResourceNotFoundException
 * @see Status#NOT_FOUND
 */
@API(status = STABLE)
public interface ResourceNotFoundAdviceTrait extends TraceableAdviceTrait {

  /**
   * handle ResourceNotFoundExceptions thrown during API calls.
   *
   * @param exception ResourceNotFoundException
   * @param request web request info
   * @return standardized error payload according to RFC7807
   */
  @API(status = INTERNAL)
  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  default ResponseEntity<Problem> handleResourceNotFound(
      final ResourceNotFoundException exception, final NativeWebRequest request) {

    return create(
        exception,
        getProblemBuilder(
                Status.NOT_FOUND,
                exception,
                request,
                new HttpHeaders(),
                URI.create(getTypeUri("not-found")))
            .build(),
        request);
  }
}
