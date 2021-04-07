package io.twdps.starter.boot.notifier;


public class NoopEntityLifecycleNotifier implements EntityLifecycleNotifier {

  private TimestampProvider provider;

  public NoopEntityLifecycleNotifier(TimestampProvider provider) {
    this.provider = provider;
  }

  public TimestampProvider getTimestampProvider() {
    return provider;
  }

  public void notify(EntityLifecycleNotification notification) {
  }

}
