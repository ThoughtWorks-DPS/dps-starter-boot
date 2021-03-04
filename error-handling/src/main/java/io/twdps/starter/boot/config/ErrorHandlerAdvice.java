package io.twdps.starter.boot.config;

import org.apiguardian.api.API;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.net.URI;

import static org.apiguardian.api.API.Status.INTERNAL;

@ControllerAdvice
public class ErrorHandlerAdvice
    implements ProblemHandling,
        ResourceNotFoundAdviceTrait,
        RequestValidationAdviceTrait {
  /**
   * handle HttpMessageNotReadableExceptions thrown during API calls.
   *
   * @param exception HttpMessageNotReadableException
   * @param request web request info
   * @return standardized error payload according to RFC7807
   */
  @API(status = INTERNAL)
  @ExceptionHandler
  public ResponseEntity<Problem> handleMessageNotReadableException(
      final HttpMessageNotReadableException exception, final NativeWebRequest request) {
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
