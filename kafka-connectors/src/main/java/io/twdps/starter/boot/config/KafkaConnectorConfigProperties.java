package io.twdps.starter.boot.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "starter.boot.kafka-connector.producer")
@Getter
@Setter
@NoArgsConstructor
public class KafkaConnectorConfigProperties {

  private int corePoolSize = 3;
  private int maxPoolSize = 3;
  private int queueCapacity = 500;
  private String threadNamePrefix = "kafkaProducerExecutor-";

  //private int batchSize;
  //private int deliveryTimeout;
  //private int maxInflightRequests;
  //private int retries;
  //private long bufferMemory;
  //private String acks;
  //private String clientId;
  //private String compressionType;

}
