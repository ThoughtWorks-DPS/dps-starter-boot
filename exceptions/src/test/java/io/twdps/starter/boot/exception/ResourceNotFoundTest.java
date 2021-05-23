package io.twdps.starter.boot.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ResourceNotFoundTest {

  private String uuid = "UUID-UUID-UUID";
  private String message = String.format("Resource '%s' not found", uuid);

  @Test
  public void constructException() {
    try {
      throw new ResourceNotFoundException(uuid);
    } catch (ResourceNotFoundException ex) {
      assertThat(ex.getMessage()).isEqualTo(message);
    }
  }

  @Test
  public void catchGenericException() {
    try {
      throw new ResourceNotFoundException(uuid);
    } catch (Exception ex) {
      assertThat(ex.getMessage()).isEqualTo(message);
    }
  }
}
