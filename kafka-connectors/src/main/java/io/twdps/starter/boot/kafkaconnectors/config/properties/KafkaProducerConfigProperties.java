package io.twdps.starter.boot.kafkaconnectors.config.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaProducerConfigProperties {

  private KafkaTopicConfigProperties topic;
  private KafkaReplicationConfigProperties replication;
  private KafkaPartitionConfigProperties partition;
}
