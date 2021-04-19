package io.twdps.starter.boot.exception;

public class DownstreamTimeoutException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DownstreamTimeoutException(String code, String message) {
    super(message);
  }
}
