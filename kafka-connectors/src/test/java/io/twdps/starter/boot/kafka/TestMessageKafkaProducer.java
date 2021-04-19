package io.twdps.starter.boot.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestMessageKafkaProducer extends KafkaProducer<TestMessage> {

  public TestMessageKafkaProducer(KafkaTemplate<Integer, TestMessage> kafkaTemplate) {
    super(kafkaTemplate);
  }
}
