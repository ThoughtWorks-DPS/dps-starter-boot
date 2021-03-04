package io.twdps.starter.boot.config;

import io.twdps.starter.boot.exception.RequestValidationException;
import org.apiguardian.api.API;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;

/**
 * AdviceTraits for handling exceptions during API calls.
 *
 * @see RequestValidationException
 * @see Status#BAD_REQUEST
 */
@API(status = STABLE)
public interface RequestValidationAdviceTrait extends TraceableAdviceTrait {

  /**
   * handle RequestValidationExceptions thrown during API calls.
   *
   * @param exception RequestValidationException
   * @param request web request info
   * @return standardized error payload according to RFC7807
   */
  @API(status = INTERNAL)
  @ExceptionHandler
  default ResponseEntity<Problem> handleRequestValidation(
      final RequestValidationException exception, final NativeWebRequest request) {
    return create(
        exception,
        getProblemBuilder(
                Status.BAD_REQUEST,
                exception,
                request,
                new HttpHeaders(),
                URI.create(getTypeUri("request-validation")))
            .build(),
        request);
  }
}