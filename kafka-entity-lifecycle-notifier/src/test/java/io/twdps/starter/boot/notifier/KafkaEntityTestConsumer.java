package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class KafkaEntityTestConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private EntityLifecycleNotification payload = null;
  private ObjectMapper mapper;

  public KafkaEntityTestConsumer(ObjectMapper objectMapper) {
    this.mapper = objectMapper;
  }
  /**
   * receive messages from Kafka topic.
   *
   * @param consumerRecord kafka consumer record from queue
   */
//  @KafkaListener(topics = "${test.topic}")
  /*
  @KafkaListener(topics = "embedded-test-topic")
  public void receive(ConsumerRecord<?, ?> consumerRecord) {
    log.info("received payload='{}'", consumerRecord.toString())
    setPayload(consumerRecord.toString());
    latch.countDown();
  }*/

  /**
   * receive messages from Kafka topic.
   *
   * @param payload   kafka consumer record from queue
   * @param topic     topic name
   * @param partition partition number
   * @param offset    offset position in queue
   */
  @KafkaListener(topics = "${test.topic}",
      concurrency = "${spring.kafka.consumer.level.concurrency:3}")
  public void logKafkaMessages(@Payload EntityLifecycleNotification payload,
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

  public EntityLifecycleNotification getPayload() {
    return payload;
  }

  public void setPayload(EntityLifecycleNotification payload) {
    this.payload = payload;
  }
}