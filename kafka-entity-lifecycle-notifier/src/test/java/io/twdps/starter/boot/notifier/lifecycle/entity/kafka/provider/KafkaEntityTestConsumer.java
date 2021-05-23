package io.twdps.starter.boot.notifier.lifecycle.entity.kafka.provider;

import io.twdps.starter.boot.notifier.lifecycle.entity.kafka.model.KafkaEntityLifecycleNotification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@Getter
@Setter
public class KafkaEntityTestConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private KafkaEntityLifecycleNotification payload = null;

  /**
   * receive messages from Kafka topic.
   *
   * @param payload kafka consumer record from queue
   * @param topic topic name
   * @param partition partition number
   * @param offset offset position in queue
   */
  @KafkaListener(
      topics = "${starter.boot.kafka-lifecycle-notifier.consumer.topic.name}",
      concurrency = "${spring.kafka.consumer.level.concurrency:3}")
  public void logKafkaMessages(
      @Payload KafkaEntityLifecycleNotification payload,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
      @Header(KafkaHeaders.OFFSET) Long offset) {
    log.info(
        "Received a message contains a payload [{}], from [{}] topic, "
            + "[{}] partition, and [{}] offset",
        payload,
        topic,
        partition,
        offset);
    setPayload(payload);
    latch.countDown();
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
