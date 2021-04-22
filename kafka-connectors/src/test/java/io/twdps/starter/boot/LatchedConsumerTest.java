package io.twdps.starter.boot;

import io.twdps.starter.boot.kafka.LatchedKafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LatchedConsumerTest {

  private LatchedKafkaConsumer<String> latched = new LatchedKafkaConsumer<>() {
    public Logger getLogger() {
      return log;
    }
  };

  @Test
  void testLatch() {
    latched.handleMessage("foo", "topic", 1, 1L);
    assertThat(latched.getLatch().getCount()).isEqualTo(0);

    latched.resetLatch();
    assertThat(latched.getLatch().getCount()).isEqualTo(1);

    latched.handleMessage("foo", "topic", 1, 1L);
    assertThat(latched.getLatch().getCount()).isEqualTo(0);
  }


}
