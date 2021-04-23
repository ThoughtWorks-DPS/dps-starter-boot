package io.twdps.starter.boot.kafka;

import io.twdps.starter.boot.config.TestKafkaProducerConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestMessageKafkaProducer extends KafkaProducer<KafkaTestMessage> {

  public TestMessageKafkaProducer(KafkaTemplate<Integer, KafkaTestMessage> kafkaTemplate,
      TestKafkaProducerConfigProperties configProperties) {
    super(kafkaTemplate, configProperties);
    log.warn("Creating producer on topic [{}]", configProperties.getTopic().getName());
  }

  @Bean
  @Order(-1)
  public NewTopic createTestTopic() {
    return super.createNewTopic();
  }
}
