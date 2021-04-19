package io.twdps.starter.boot.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
