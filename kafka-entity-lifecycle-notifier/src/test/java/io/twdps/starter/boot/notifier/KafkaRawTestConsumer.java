package io.twdps.starter.boot.notifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class KafkaRawTestConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private String payload = null;

  /**
   * receive messages from Kafka topic.
   *
   * @param payload   kafka consumer record from queue
   * @param topic     topic name
   * @param partition partition number
   * @param offset    offset position in queue
   */
  @KafkaListener(topics = "${starter.boot.kafka-lifecycle-notifier.queue-name}",
      concurrency = "${spring.kafka.consumer.level.concurrency:3}")
  public void logKafkaMessages(@Payload String payload,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
      @Header(KafkaHeaders.OFFSET) Long offset) {
    log.info("Received a message contains a payload [{}], from [{}] topic, "
        + "[{}] partition, and [{}] offset", payload, topic, partition, offset);
    setPayload(payload);
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}
