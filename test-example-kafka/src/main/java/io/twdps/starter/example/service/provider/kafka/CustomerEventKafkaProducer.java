package io.twdps.starter.example.service.provider.kafka;

import io.twdps.starter.boot.kafkaconnectors.provider.KafkaProducer;
import io.twdps.starter.example.service.spi.kafka.model.CustomerEventMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerEventKafkaProducer extends KafkaProducer<CustomerEventMessage> {

  public CustomerEventKafkaProducer(
      KafkaTemplate<Integer, CustomerEventMessage> kafkaTemplate,
      CustomerEventKafkaProducerConfigProperties configProperties) {
    super(kafkaTemplate, configProperties);
  }
}
