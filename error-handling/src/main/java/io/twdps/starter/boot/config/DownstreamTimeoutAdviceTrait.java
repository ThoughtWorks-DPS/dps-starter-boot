package io.twdps.starter.boot.config;

import io.twdps.starter.boot.exception.DownstreamTimeoutException;
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
 * @see DownstreamTimeoutException
 * @see Status#REQUEST_TIMEOUT
 */
@API(status = STABLE)
public interface DownstreamTimeoutAdviceTrait extends TraceableAdviceTrait {

  /**
   * handle RequestValidationExceptions thrown during API calls.
   *
   * @param exception DownstreamTimeoutException
   * @param request web request info
   * @return standardized error payload according to RFC7807
   */
  @API(status = INTERNAL)
  @ExceptionHandler
  @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
  default ResponseEntity<Problem> handleRequestValidation(
      final DownstreamTimeoutException exception, final NativeWebRequest request) {
    return create(
        exception,
        getProblemBuilder(
                Status.REQUEST_TIMEOUT,
                exception,
                request,
                new HttpHeaders(),
                URI.create(getTypeUri("request-timeout")))
            .build(),
        request);
  }
}
