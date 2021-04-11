package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.twdps.starter.boot.notifier.CurrentTimestampProvider;
import io.twdps.starter.boot.notifier.EntityDescriptor;
import io.twdps.starter.boot.notifier.EntityDescriptorDeserializer;
import io.twdps.starter.boot.notifier.EntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.KafkaEntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.TimestampProvider;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConditionalOnProperty(prefix = "starter.boot.kafka-lifecycle-notifier",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class KafkaEntityLifecycleNotifierConfig {

  private final Logger log = LoggerFactory.getLogger(KafkaEntityLifecycleNotifierConfig.class);

  private final KafkaEntityLifecycleNotifierConfigProperties configProperties;
  private final KafkaEntityLifecycleNotifier notifier;

  /**
   * Constructor for configuration provider.
   *
   * @param properties entity lifecycle notifier config bean
   * @param notifier   kafka notifier bean
   */
  public KafkaEntityLifecycleNotifierConfig(
      KafkaEntityLifecycleNotifier notifier,
      KafkaEntityLifecycleNotifierConfigProperties properties) {
    log.warn("Constructing KafkaEntityLifecycleNotifierConfig");
    this.configProperties = properties;
    this.notifier = notifier;
  }

  /**
   * Provide KafkaEntityLifecycleNotifier.
   * This function just returns the ctor-autowired notifier. Create a derived configuration class
   * and override the function to provide your own Kafka notifier bean.
   *
   * @return autowired notifier bean
   */
  @Bean
  public EntityLifecycleNotifier lifecycleNotifier() {
    return notifier;
  }

  /**
   * Configure ObjectMapper to serialize dates as strings.
   * This only occurs if there are no other ObjectMapper beans configured. If you provide your own
   * bean, you must also configure it to (de)serialize dates as expected.
   *
   * @return configured ObjectMapper
   */
  @ConditionalOnMissingBean
  @Bean
  public ObjectMapper configureObjectMapper() {
    log.warn("Configuring Object Mapper");
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addDeserializer(EntityDescriptor.class, new EntityDescriptorDeserializer());
    mapper.registerModule(module);

    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
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
