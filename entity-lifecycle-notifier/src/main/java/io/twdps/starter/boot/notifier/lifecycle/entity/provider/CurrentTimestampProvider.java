package io.twdps.starter.boot.notifier.lifecycle.entity.provider;

import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;

import java.time.ZonedDateTime;

public class CurrentTimestampProvider implements TimestampProvider {

  public ZonedDateTime now() {
    return ZonedDateTime.now();
  }
}
