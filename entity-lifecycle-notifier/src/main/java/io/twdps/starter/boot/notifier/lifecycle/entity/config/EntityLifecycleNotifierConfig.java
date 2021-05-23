package io.twdps.starter.boot.notifier.lifecycle.entity.config;

import io.twdps.starter.boot.notifier.lifecycle.entity.provider.CurrentTimestampProvider;
import io.twdps.starter.boot.notifier.lifecycle.entity.provider.NoopEntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.EntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Provide default beans necessary for lifecycle entity notifications. */
@Configuration
public class EntityLifecycleNotifierConfig {

  /**
   * Provide default timestamp provider for determining current timestamp.
   *
   * @return CurrentTimestampProvider
   */
  @ConditionalOnMissingBean
  @Bean
  public TimestampProvider timestampProvider() {
    return new CurrentTimestampProvider();
  }

  /**
   * Provide default notifier bean as a do-nothing noop notifier. This notifier just swallows the
   * notifications.
   *
   * @return NoopEntityLifecycleNotifier
   */
  @ConditionalOnMissingBean
  @Bean
  public EntityLifecycleNotifier lifecycleNotifier() {
    return new NoopEntityLifecycleNotifier(timestampProvider());
  }
}
