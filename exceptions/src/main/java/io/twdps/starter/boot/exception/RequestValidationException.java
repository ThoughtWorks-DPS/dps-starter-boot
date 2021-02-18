package io.twdps.starter.boot.exception;

public class RequestValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public RequestValidationException(String resourceId) {
    super(String.format("Resource '%s' invalid request", resourceId));
  }
}
