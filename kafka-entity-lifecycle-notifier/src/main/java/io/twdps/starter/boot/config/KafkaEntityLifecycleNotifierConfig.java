package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.kafka.KafkaProducer;
import io.twdps.starter.boot.notifier.CurrentTimestampProvider;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification;
import io.twdps.starter.boot.notifier.EntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.KafkaEntityLifecycleNotifier;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "starter.boot.kafka-lifecycle-notifier")
@ConditionalOnProperty(prefix = "starter.boot.kafka-lifecycle-notifier",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class KafkaEntityLifecycleNotifierConfig {

  @Value("${queue-name:entity-lifecycle-notifier}")
  private String topicName;

  @Value("${spring.kafka.replication.factor:1}")
  private int replicationFactor;

  @Value("${spring.kafka.partition.number:1}")
  private int partitionNumber;

  @Autowired
  private KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate;

  @Bean
  public EntityLifecycleNotifier lifecycleNotifier() {
    return new KafkaEntityLifecycleNotifier(kafkaTemplate, new CurrentTimestampProvider(),
        topicName);
  }

  @Bean
  @Order(-1)
  public NewTopic createNewTopic() {
    return new NewTopic(topicName, partitionNumber, (short) replicationFactor);
  }

}
