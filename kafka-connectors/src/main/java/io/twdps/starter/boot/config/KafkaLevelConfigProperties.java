package io.twdps.starter.boot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaLevelConfigProperties {
  private int concurrency;

}
