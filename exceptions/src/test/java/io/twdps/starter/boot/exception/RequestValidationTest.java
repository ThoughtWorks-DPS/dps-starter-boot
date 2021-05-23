package io.twdps.starter.boot.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RequestValidationTest {

  @Test
  public void constructException() {
    String resourceId = "foo";
    String message = "Resource 'foo' invalid request";

    try {
      throw new RequestValidationException(resourceId);
    } catch (RequestValidationException ex) {
      assertThat(ex.getMessage()).isEqualTo(message);
    }
  }

  @Test
  public void catchGenericException() {
    String resourceId = "foo";
    String message = "Resource 'foo' invalid request";

    try {
      throw new RequestValidationException(resourceId);
    } catch (Exception ex) {
      assertThat(ex.getMessage()).isEqualTo(message);
    }
  }
}
