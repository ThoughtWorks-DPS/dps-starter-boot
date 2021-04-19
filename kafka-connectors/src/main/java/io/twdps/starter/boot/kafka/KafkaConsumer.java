package io.twdps.starter.boot.kafka;

import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public interface KafkaConsumer<T> {

  Logger getLogger();

  /**
   * receive messages from Kafka topic.
   *
   * @param payload   kafka consumer record from queue
   * @param topic     topic name
   * @param partition partition number
   * @param offset    offset position in queue
   */
  @KafkaListener(topics = "${spring.kafka.topic.name}",
      concurrency = "${spring.kafka.consumer.level.concurrency:3}")
  default void receiveKafkaMessages(@Payload T payload,
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
  default void handleMessage(T payload, String topic, Integer partition, Long offset) {
    getLogger().info("Received a message with topic: [{}] "
        + "partition: [{}] offset: [{}] payload: [{}] ", topic, partition, offset, payload);
    handleMessage(payload);
  }

  default void handleMessage(T payload) {}
}
