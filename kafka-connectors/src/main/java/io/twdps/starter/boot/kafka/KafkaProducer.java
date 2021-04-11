package io.twdps.starter.boot.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${spring.kafka.topic.name}")
  private String topicName;

  @Value("${spring.kafka.replication.factor:1}")
  private int replicationFactor;

  @Value("${spring.kafka.partition.number:1}")
  private int partitionNumber;

  public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(String payload) {
    log.info("sending payload='{}'", payload);
    kafkaTemplate.send(topicName, payload);
  }

  public void send(String topic, String payload) {
    log.info("sending payload='{}' to topic='{}'", payload, topic);
    kafkaTemplate.send(topic, "key", payload);
  }

  @Bean
  @Order(-1)
  public NewTopic createNewTopic() {
    return new NewTopic(topicName, partitionNumber, (short) replicationFactor);
  }
}
