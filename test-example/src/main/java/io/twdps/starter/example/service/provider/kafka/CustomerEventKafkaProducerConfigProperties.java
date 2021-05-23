package io.twdps.starter.example.service.provider.kafka;

import io.twdps.starter.boot.kafkaconnectors.config.properties.KafkaProducerConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "starter.boot.customer-event.producer")
public class CustomerEventKafkaProducerConfigProperties extends KafkaProducerConfigProperties {}
