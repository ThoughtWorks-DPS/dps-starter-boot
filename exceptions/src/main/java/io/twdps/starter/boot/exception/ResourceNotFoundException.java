package io.twdps.starter.boot.exception;

public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(String resourceId) {
    super(String.format("Resource '%s' not found", resourceId));
  }
}
