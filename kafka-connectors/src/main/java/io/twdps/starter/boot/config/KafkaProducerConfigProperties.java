package io.twdps.starter.boot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
public class KafkaProducerConfigProperties {

  private KafkaTopicConfigProperties topic;
  private KafkaReplicationConfigProperties replication;
  private KafkaPartitionConfigProperties partition;

}
