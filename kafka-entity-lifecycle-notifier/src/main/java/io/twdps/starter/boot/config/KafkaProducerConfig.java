package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Configure the KafkaTemplate so we can serialize dates properly as strings.
   *
   * @return configured KafkaTemplate
   */
  @Bean
  public KafkaTemplate<String, EntityLifecycleNotification>
  entityLifecycleNotificationKafkaTemplate() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

    ProducerFactory<String, EntityLifecycleNotification> producerFactory =
        new DefaultKafkaProducerFactory<>(props, new StringSerializer(),
            new JsonSerializer<EntityLifecycleNotification>(objectMapper));

    return new KafkaTemplate<>(producerFactory);
  }
}
