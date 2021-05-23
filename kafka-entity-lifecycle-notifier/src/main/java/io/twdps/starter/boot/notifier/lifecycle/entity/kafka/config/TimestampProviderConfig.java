package io.twdps.starter.boot.notifier.lifecycle.entity.kafka.config;

import io.twdps.starter.boot.notifier.lifecycle.entity.provider.CurrentTimestampProvider;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(
    name = {
      "io.twdps.starter.boot.notifier.lifecycle.entity.kafka.config.KafkaEntityLifecycleNotifierConfig"
    })
public class TimestampProviderConfig {

  @ConditionalOnMissingBean
  @Bean
  public TimestampProvider timestampProvider() {
    return new CurrentTimestampProvider();
  }
}
