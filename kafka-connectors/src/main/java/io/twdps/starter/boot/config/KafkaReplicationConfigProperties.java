package io.twdps.starter.boot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaReplicationConfigProperties {
  private int factor;

}
