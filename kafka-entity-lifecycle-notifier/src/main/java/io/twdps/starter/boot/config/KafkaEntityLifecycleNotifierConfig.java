package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.twdps.starter.boot.notifier.CurrentTimestampProvider;
import io.twdps.starter.boot.notifier.EntityDescriptor;
import io.twdps.starter.boot.notifier.EntityDescriptorDeserializer;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification;
import io.twdps.starter.boot.notifier.EntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.KafkaEntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.TimestampProvider;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@ConditionalOnProperty(prefix = "starter.boot.kafka-lifecycle-notifier",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@AutoConfigureBefore(name = {"io.twdps.starter.boot.config.EntityLifecycleNotifierConfig"})
public class KafkaEntityLifecycleNotifierConfig {

  private final Logger log = LoggerFactory.getLogger(KafkaEntityLifecycleNotifierConfig.class);

  private final KafkaEntityLifecycleNotifierConfigProperties configProperties;
  private final KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate;
  private final TimestampProvider provider;
  private final String queueName;
  private final KafkaEntityLifecycleNotifier notifier;

  /**
   * Constructor for configuration provider.
   *
   * @param properties    entity lifecycle notifier config bean
   * @param kafkaTemplate KafkaTemplate<> for sending the notifications
   * @param provider      Timestamp Provider (for testing)
   * @param queueName     name of the kafka queue
   */
  public KafkaEntityLifecycleNotifierConfig(
      KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate,
      TimestampProvider provider,
      @Value("${starter.boot.kafka-lifecycle-notifier.queue-name:entity-lifecycle-notifier}") String queueName,
      KafkaEntityLifecycleNotifierConfigProperties properties) {
    log.warn("Constructing KafkaEntityLifecycleNotifierConfig");
    this.configProperties = properties;
    this.kafkaTemplate = kafkaTemplate;
    this.provider = provider;
    this.queueName = queueName;
    this.notifier = new KafkaEntityLifecycleNotifier(this.kafkaTemplate, this.provider,
        this.queueName);
  }

  /**
   * Provide KafkaEntityLifecycleNotifier. This function just returns the ctor-autowired notifier.
   * Create a derived configuration class and override the function to provide your own Kafka
   * notifier bean.
   *
   * @return autowired notifier bean
   */
  @Bean
  public EntityLifecycleNotifier kafkaEntityLifecycleNotifierBean() {
    return notifier;
  }

  /**
   * Create the required topic in kafka.
   *
   * @return NewTopic bean
   */
  @Bean
  @Order(-1)
  public NewTopic createNewTopic() {
    return new NewTopic(configProperties.getTopicName(),
        configProperties.getPartitionNumber(),
        (short) configProperties.getReplicationFactor());
  }

}
