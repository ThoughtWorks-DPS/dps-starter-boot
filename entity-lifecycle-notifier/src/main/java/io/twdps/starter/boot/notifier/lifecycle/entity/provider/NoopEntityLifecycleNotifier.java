package io.twdps.starter.boot.notifier.lifecycle.entity.provider;

import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityLifecycleNotification;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.EntityLifecycleNotifier;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;

public class NoopEntityLifecycleNotifier implements EntityLifecycleNotifier {

  private TimestampProvider provider;

  public NoopEntityLifecycleNotifier(TimestampProvider provider) {
    this.provider = provider;
  }

  public TimestampProvider getTimestampProvider() {
    return provider;
  }

  public void notify(EntityLifecycleNotification notification) {}
}
