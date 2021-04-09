package io.twdps.starter.boot.notifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

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
