package io.twdps.starter.boot.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
public class KafkaProducer<T extends IdentifiableMessage> {

  private final KafkaTemplate<Integer, T> kafkaTemplate;

  @Value("${spring.kafka.topic.name}")
  private String topicName;

  @Value("${spring.kafka.replication.factor:1}")
  private int replicationFactor;

  @Value("${spring.kafka.partition.number:1}")
  private int partitionNumber;

  public KafkaProducer(KafkaTemplate<Integer, T> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(T payload) {
    log.info("sending payload='{}'", payload);
    kafkaTemplate.send(topicName, payload);
  }

  public void send(String topic, T payload) {
    log.info("sending payload='{}' to topic='{}'", payload, topic);
    sendMessage(topic, payload);
  }

  public ListenableFuture<SendResult<Integer, T>> sendMessage(final T message) {
    log.info("(async) sending payload='{}'", message);
    return sendMessage(topicName, message);
  }

  /**
   * send a message async and return listenable future for determining results.
   *
   * @param topic topic on which to send message
   * @param message message object to send
   * @return listenable future for send status
   */
  @Async("kafkaProducerExecutor")
  public ListenableFuture<SendResult<Integer, T>> sendMessage(String topic, final T message) {
    log.info("(async) sending payload='{}' to topic='{}'", message, topic);
    ListenableFuture<SendResult<Integer, T>> future = this.kafkaTemplate.send(topic,
        message.getMessageIdentifier(), message);
    return future;
  }

  @Bean
  @Order(-1)
  public NewTopic createNewTopic() {
    return new NewTopic(topicName, partitionNumber, (short) replicationFactor);
  }
}