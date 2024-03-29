package io.twdps.starter.boot.kafkaconnectors.provider;

import io.twdps.starter.boot.kafkaconnectors.config.properties.AlternateKafkaProducerConfigProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
    prefix = "starter.boot.test-topic-creation",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = false)
public class AlternateMessageKafkaProducer extends KafkaProducer<KafkaTestMessage> {

  public AlternateMessageKafkaProducer(
      KafkaTemplate<Integer, KafkaTestMessage> kafkaTemplate,
      AlternateKafkaProducerConfigProperties configProperties) {
    super(kafkaTemplate, configProperties);
  }

  @Bean
  @Order(-1)
  public NewTopic createAlternateTopic() {
    return super.createNewTopic();
  }
}
