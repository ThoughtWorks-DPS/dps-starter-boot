package io.twdps.starter.boot.config;

import brave.Tracer;
import io.twdps.starter.boot.exception.RequestValidationException;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.AdviceTrait;

import java.net.URI;
import java.net.URISyntaxException;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;

/** Base class for extracting OpenTracing info from request headers. */
@API(status = STABLE)
public interface TraceableAdviceTrait extends AdviceTrait {

  @API(status = INTERNAL)
  default String getUriRoot() {
    return "https://starter.twdps.io";
  }

  @API(status = INTERNAL)
  default String getTypeUri(String typename) {
    return String.format("%s/%s", getUriRoot(), typename);
  }

  @API(status = INTERNAL)
  default String getTraceHeaderName() {
    return "X-B3-TraceId";
  }

  /**
   * extract opentracing trace/span info and return a URI.
   *
   * @param request web request
   * @param headers http headers
   * @return URI containing trace/span info
   */
  @API(status = INTERNAL)
  default URI extractInstance(final NativeWebRequest request, final HttpHeaders headers) {
    // TODO
    try {
      String trace = "NOTRACE";
      if (null != request) {
        String[] traceHeaders = request.getHeaderValues(getTraceHeaderName());
        if (null != traceHeaders) {
          trace = traceHeaders[0];
        }
      }
      return new URI(String.format("%s/%s", getUriRoot(), trace));
    } catch (URISyntaxException ex) {
      // What to report here?
      return null;
    }
  }

  /**
   * construct a ProblemBuilder using standard *Advice foundational methods.
   *
   * @param status result of API call (http status codes)
   * @param throwable the exception thrown
   * @param request the web request info
   * @param headers additional headers
   * @param type the URI type of the error payload
   * @return a ProblemBuilder that can be further modified before finalizing the Problem object
   */
  @API(status = INTERNAL)
  default ProblemBuilder getProblemBuilder(
      final StatusType status,
      final Throwable throwable,
      final NativeWebRequest request,
      final HttpHeaders headers,
      final URI type) {

    Problem base = toProblem(throwable, status, type);
    return Problem.builder()
        .withDetail(base.getDetail())
        .withTitle(base.getTitle())
        .withStatus(base.getStatus())
        .withType(base.getType())
        .withInstance(extractInstance(request, headers));
  }
}
