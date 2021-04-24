package io.twdps.starter.boot;

import io.twdps.starter.boot.config.AlternateKafkaProducerConfigProperties;
import io.twdps.starter.boot.config.SpringKafkaProducerConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-alternate.yml"})
public class KafkaProducerConfigPropertiesTest {

  @Autowired
  private SpringKafkaProducerConfigProperties springKafkaProducerConfigProperties;

  @Autowired
  private AlternateKafkaProducerConfigProperties testConfigProperties;

  @Value("${spring.kafka.topic.name}")
  private String springTopicName;

  @Value("${starter.boot.alternate-kafka-connector.producer.topic.name}")
  private String testTopicName;

  @Test
  public void valueAnnotationPopulated() {
    assertThat(springTopicName).isNotNull();
    assertThat(springTopicName).isEqualTo("embedded-test-topic");
    assertThat(testTopicName).isNotNull();
    assertThat(testTopicName).isEqualTo("alternate-test-topic");
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
