package io.twdps.starter.boot.tracing.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class NoopTest {

  @Test
  public void setupValid() {
    assertThat(true).isTrue();
  }

  @Test
  public void setupInvalid() {
    assertThat(false).isFalse();
  }
}
