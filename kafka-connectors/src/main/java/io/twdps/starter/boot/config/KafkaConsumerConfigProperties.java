package io.twdps.starter.boot.config;

import lombok.Getter;
import lombok.Setter;

/**
 * This class isn't used (yet) primarily for lack of determining a good way to allow
 * the configuration to be referenced by the @KafkaListener() annotation for configuring
 * the listening topic.
 */
@Getter
@Setter
public class KafkaConsumerConfigProperties {

  private KafkaTopicConfigProperties topic;
  private KafkaLevelConfigProperties level;
}
