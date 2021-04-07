package io.twdps.starter.boot.notifier;


public class MulticastEntityLifecycleNotifier implements EntityLifecycleNotifier {

  private TimestampProvider provider;

  public MulticastEntityLifecycleNotifier(TimestampProvider provider) {
    this.provider = provider;
  }

  public TimestampProvider getTimestampProvider() {
    return provider;
  }

  public void notify(EntityLifecycleNotification notification) {
  }

}
