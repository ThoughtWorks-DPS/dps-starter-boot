package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
public class KafkaEntityLifecycleNotifier implements EntityLifecycleNotifier {

  private TimestampProvider provider;
  private final KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate;

  private String queueName;

  @Value("${spring.kafka.replication.factor:1}")
  private int replicationFactor;

  @Value("${spring.kafka.partition.number:1}")
  private int partitionNumber;

  public KafkaEntityLifecycleNotifier(
      KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate,
      TimestampProvider provider, String queueName) {
    this.kafkaTemplate = kafkaTemplate;
    this.queueName = queueName;
    this.provider = provider;
  }

  public TimestampProvider getTimestampProvider() {
    return provider;
  }

  public void notify(EntityLifecycleNotification notification) {
    log.info("notify: [{}]", notification.toString());
    log.info("sending payload='{}' to topic='{}'", notification, queueName);
    kafkaTemplate.send(queueName, "key", notification);
  }

}
