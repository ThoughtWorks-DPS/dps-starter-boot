package io.twdps.starter.boot.kafkaconnectors.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@EnableKafka
@Configuration
@ConditionalOnProperty(
    prefix = "starter.boot.kafka-connector.consumer",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class KafkaConsumerConfig {

  public static Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

  /**
   * configure kafka listener container factory.
   *
   * @return configured bean
   */
  @Bean
  // CSOFF: LineLength
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, Object>>
      kafkaListenerContainerFactory(ConsumerFactory<Integer, Object> consumerFactory) {
    // CSON: LineLength
    logger.info("=========== creating KafkaListenerContainerFactory");
    ConcurrentKafkaListenerContainerFactory<Integer, Object> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }
}
