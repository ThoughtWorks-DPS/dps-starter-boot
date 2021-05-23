package io.twdps.starter.boot.kafkaconnectors.provider;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Getter
@Setter
@Slf4j
public class TestMessageKafkaConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private KafkaTestMessage payload = null;

  public TestMessageKafkaConsumer(
      @Value("${starter.boot.kafka-connector.consumer.topic.name}") String topic) {
    log.warn("Creating consumer on topic [{}]", topic);
  }

  /**
   * receive messages from Kafka topic.
   *
   * @param payload kafka consumer record from queue
   * @param topic topic name
   * @param partition partition number
   * @param offset offset position in queue
   */
  @KafkaListener(
      topics = "${starter.boot.kafka-connector.consumer.topic.name}",
      concurrency = "${starter.boot.kafka-connector.consumer.level.concurrency:1}")
  void receiveKafkaMessages(
      @Payload KafkaTestMessage payload,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
      @Header(KafkaHeaders.OFFSET) Long offset) {
    handleMessage(payload, topic, partition, offset);
  }

  /**
   * message handler, logs full kafka details and passes to simpler handleMessage().
   *
   * @param payload message object
   * @param topic topic on which it appeared
   * @param partition partition on which it appeared
   * @param offset offset in history
   */
  public void handleMessage(
      KafkaTestMessage payload, String topic, Integer partition, Long offset) {
    log.info(
        "Received a message with topic: [{}] " + "partition: [{}] offset: [{}] payload: [{}] ",
        topic,
        partition,
        offset,
        payload);
    setPayload(payload);
    latch.countDown();
    log.info("latch: [{}]", latch.getCount());
    // pass it on
    handleMessage(payload);
  }

  void handleMessage(KafkaTestMessage payload) {}

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
