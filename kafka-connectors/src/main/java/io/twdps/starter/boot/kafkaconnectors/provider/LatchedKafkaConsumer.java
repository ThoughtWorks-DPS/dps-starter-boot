package io.twdps.starter.boot.kafkaconnectors.provider;

import io.twdps.starter.boot.kafkaconnectors.spi.KafkaConsumer;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;

@Getter
@Setter
public abstract class LatchedKafkaConsumer<T> implements KafkaConsumer<T> {

  private CountDownLatch latch = new CountDownLatch(1);
  private T payload = null;

  @Override
  public void handleMessage(T payload, String topic, Integer partition, Long offset) {
    getLogger().info("Handling message [{}]", payload);
    setPayload(payload);
    latch.countDown();
    getLogger().info("latch: [{}]", latch.getCount());
    // pass it on
    handleMessage(payload);
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
