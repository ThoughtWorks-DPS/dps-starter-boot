package io.twdps.starter.boot;

import io.twdps.starter.boot.config.SpringKafkaProducerConfigProperties;
import io.twdps.starter.boot.config.TestKafkaProducerConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KafkaProducerConfigPropertiesTest {

  @Autowired
  private SpringKafkaProducerConfigProperties springKafkaProducerConfigProperties;

  @Autowired
  private TestKafkaProducerConfigProperties testConfigProperties;

  @Value("${spring.kafka.topic.name}")
  private String springTopicName;

  @Value("${starter.boot.kafka-connector.producer.topic.name}")
  private String testTopicName;

  @Test
  public void valueAnnotationPopulated() {
    assertThat(springTopicName).isNotNull();
    assertThat(springTopicName).isEqualTo("embedded-test-topic");
    assertThat(testTopicName).isNotNull();
    assertThat(testTopicName).isEqualTo("producer-test-topic");
  }

  @Test
  public void springConfigPropertiesPopulated() {
    assertThat(springKafkaProducerConfigProperties.getTopic().getName()).isNotNull();
    assertThat(springKafkaProducerConfigProperties.getTopic().getName()).isEqualTo(springTopicName);
    assertThat(springKafkaProducerConfigProperties.getPartition().getNumber()).isEqualTo(1);
    assertThat(springKafkaProducerConfigProperties.getReplication().getFactor()).isEqualTo(1);
  }

  @Test
  public void testConfigPropertiesPopulated() {
    assertThat(testConfigProperties.getTopic().getName()).isNotNull();
    assertThat(testConfigProperties.getTopic().getName()).isEqualTo(testTopicName);
    assertThat(testConfigProperties.getPartition().getNumber()).isEqualTo(5);
    assertThat(testConfigProperties.getReplication().getFactor()).isEqualTo(1);
  }
}
