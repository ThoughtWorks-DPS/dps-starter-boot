package io.twdps.starter.example.service.provider.kafka;

import io.twdps.starter.example.service.spi.kafka.model.CustomerEventMessage;
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
public class CustomerEventKafkaConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private CustomerEventMessage payload = null;

  /**
   * receive messages from Kafka topic.
   *
   * @param payload   kafka consumer record from queue
   * @param topic     topic name
   * @param partition partition number
   * @param offset    offset position in queue
   */
  @KafkaListener(topics = "${starter.boot.customer-event.consumer.topic.name}",
      concurrency = "${starter.boot.customer-event.consumer.level.concurrency:3}")
  void receiveKafkaMessages(@Payload CustomerEventMessage payload,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
      @Header(KafkaHeaders.OFFSET) Long offset) {
    log.trace("Received a message contains a payload [{}], from [{}] topic, "
        + "[{}] partition, and [{}] offset", payload, topic, partition, offset);
    handleMessage(payload, topic, partition, offset);
  }

  /**
   * message handler, logs full kafka details and passes to simpler handleMessage().
   *
   * @param payload   message object
   * @param topic     topic on which it appeared
   * @param partition partition on which it appeared
   * @param offset    offset in history
   */
  public void handleMessage(CustomerEventMessage payload, String topic, Integer partition,
      Long offset) {
    log.debug("Received a message with topic: [{}] "
        + "partition: [{}] offset: [{}] payload: [{}] ", topic, partition, offset, payload);
    setPayload(payload);
    latch.countDown();
    log.debug("latch: [{}]", latch.getCount());
    // pass it on
    handleMessage(payload);
  }

  /**
   * message handler.
   *
   * @param event customer event message
   */
  public void handleMessage(CustomerEventMessage event) {
    // process event message
    log.debug("Received Message with event type:{}, customerID:{}",
        event.getType(), event.getCustomerId());
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
