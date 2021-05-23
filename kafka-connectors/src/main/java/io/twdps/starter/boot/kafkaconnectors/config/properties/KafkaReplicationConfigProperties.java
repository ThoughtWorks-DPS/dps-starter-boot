package io.twdps.starter.boot.kafkaconnectors.config.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaReplicationConfigProperties {
  private int factor;
}
