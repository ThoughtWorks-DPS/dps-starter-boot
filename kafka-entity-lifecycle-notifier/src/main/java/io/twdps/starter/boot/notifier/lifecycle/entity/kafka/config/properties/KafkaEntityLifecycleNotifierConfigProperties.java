package io.twdps.starter.boot.notifier.lifecycle.entity.kafka.config.properties;

import io.twdps.starter.boot.kafkaconnectors.config.properties.KafkaProducerConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "starter.boot.kafka-lifecycle-notifier.producer")
@AutoConfigureBefore(
    name = {
      "io.twdps.starter.boot.notifier.lifecycle.entity.kafka.config.KafkaEntityLifecycleNotifierConfig"
    })
public class KafkaEntityLifecycleNotifierConfigProperties extends KafkaProducerConfigProperties {}
