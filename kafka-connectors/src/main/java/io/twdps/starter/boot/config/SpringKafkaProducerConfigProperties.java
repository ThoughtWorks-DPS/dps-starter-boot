package io.twdps.starter.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class SpringKafkaProducerConfigProperties extends KafkaProducerConfigProperties {

}
