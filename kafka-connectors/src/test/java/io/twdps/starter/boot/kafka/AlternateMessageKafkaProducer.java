package io.twdps.starter.boot.kafka;

import io.twdps.starter.boot.config.AlternateKafkaProducerConfigProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlternateMessageKafkaProducer extends KafkaProducer<TestMessage> {

  public AlternateMessageKafkaProducer(KafkaTemplate<Integer, TestMessage> kafkaTemplate,
      AlternateKafkaProducerConfigProperties configProperties) {
    super(kafkaTemplate, configProperties);
  }

  @Bean
  @Order(-1)
  public NewTopic createAlternateTopic() {
    return super.createNewTopic();
  }
}
