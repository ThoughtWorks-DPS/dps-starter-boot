package io.twdps.starter.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "starter.boot.alternate-kafka-connector.producer")
public class AlternateKafkaProducerConfigProperties extends KafkaProducerConfigProperties {

}
