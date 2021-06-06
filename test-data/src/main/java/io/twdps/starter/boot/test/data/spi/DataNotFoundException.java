package io.twdps.starter.boot.test.data.spi;

public class DataNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DataNotFoundException(String name) {
    super(String.format("Unable to find data for [{}]", name));
  }
}
