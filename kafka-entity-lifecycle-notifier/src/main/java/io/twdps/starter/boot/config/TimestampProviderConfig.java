package io.twdps.starter.boot.config;

import io.twdps.starter.boot.notifier.CurrentTimestampProvider;
import io.twdps.starter.boot.notifier.TimestampProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimestampProviderConfig {

  @Bean
  public TimestampProvider timestampProvider() {
    return new CurrentTimestampProvider();
  }
}
