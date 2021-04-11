package io.twdps.starter.boot.config;

import io.twdps.starter.boot.notifier.CurrentTimestampProvider;
import io.twdps.starter.boot.notifier.EntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.NoopEntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.TimestampProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityLifecycleNotifierConfig {

  @ConditionalOnMissingBean
  @Bean
  public TimestampProvider timestampProvider() {
    return new CurrentTimestampProvider();
  }

  @ConditionalOnMissingBean
  @Bean
  public EntityLifecycleNotifier lifecycleNotifier() {
    return new NoopEntityLifecycleNotifier(timestampProvider());
  }
}
