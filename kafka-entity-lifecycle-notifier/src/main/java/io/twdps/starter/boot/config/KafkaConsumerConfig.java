package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value(value = "${spring.kafka.consumer.group-id}")
  private String groupId;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Configure the Kafka Listener so we can serialize dates properly as strings.
   *
   * @return configured Listener Factory
   */
  @Bean
  // CSOFF: LineLength
  public ConcurrentKafkaListenerContainerFactory<String, EntityLifecycleNotification> entityLifecycleNotificationKafkaListenerContainerFactory() {
    // CSON: LineLength
    ConcurrentKafkaListenerContainerFactory<String, EntityLifecycleNotification> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    ConsumerFactory<String, EntityLifecycleNotification> consumerFactory =
        new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
            new JsonDeserializer<>(EntityLifecycleNotification.class, objectMapper));

    factory.setConsumerFactory(consumerFactory);
    return factory;
  }

}
