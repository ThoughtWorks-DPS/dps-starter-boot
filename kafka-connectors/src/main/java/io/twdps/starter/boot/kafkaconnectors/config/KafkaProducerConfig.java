package io.twdps.starter.boot.kafkaconnectors.config;

import io.twdps.starter.boot.kafkaconnectors.config.properties.KafkaConnectorConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableKafka
@Configuration
@ConditionalOnProperty(
    prefix = "starter.boot.kafka-connector.producer",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class KafkaProducerConfig {

  public static Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);

  @Autowired private KafkaConnectorConfigProperties producerConfigs;

  /**
   * create the executor thread pool for producer beans.
   *
   * @return executor object
   */
  @Bean(name = "kafkaProducerExecutor")
  public Executor kafkaProducerExecutor() {
    logger.info("=========== creating Executor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(producerConfigs.getCorePoolSize());
    executor.setMaxPoolSize(producerConfigs.getMaxPoolSize());
    executor.setQueueCapacity(producerConfigs.getQueueCapacity());
    executor.setThreadNamePrefix(producerConfigs.getThreadNamePrefix());
    executor.initialize();
    return executor;
  }
}
