package io.twdps.starter.boot.notifier.lifecycle.entity.kafka.config.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaEntityLifecycleNotifierConfigPropertiesTest {

  @Autowired private KafkaEntityLifecycleNotifierConfigProperties configProperties;

  @Value("${starter.boot.kafka-lifecycle-notifier.producer.topic.name}")
  private String testTopicName;

  @Test
  public void springConfigPropertiesPopulated() {
    assertThat(configProperties.getTopic().getName()).isNotNull();
    assertThat(configProperties.getTopic().getName()).isEqualTo(testTopicName);
    assertThat(configProperties.getPartition().getNumber()).isEqualTo(1);
    assertThat(configProperties.getReplication().getFactor()).isEqualTo(1);
  }
}
