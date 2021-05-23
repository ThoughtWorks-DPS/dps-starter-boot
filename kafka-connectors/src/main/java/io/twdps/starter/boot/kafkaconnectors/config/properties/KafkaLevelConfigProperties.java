package io.twdps.starter.boot.kafkaconnectors.config.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaLevelConfigProperties {
  private int concurrency;
}
