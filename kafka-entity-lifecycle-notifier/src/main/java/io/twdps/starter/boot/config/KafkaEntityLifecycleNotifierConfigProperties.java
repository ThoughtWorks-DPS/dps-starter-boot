package io.twdps.starter.boot.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaEntityLifecycleNotifierConfigProperties {

  @Value("${starter.boot.kafka-lifecycle-notifier.queue-name:entity-lifecycle-notifier}")
  private String topicName;

  @Value("${spring.kafka.replication.factor:1}")
  private int replicationFactor;

  @Value("${spring.kafka.partition.number:1}")
  private int partitionNumber;

}
