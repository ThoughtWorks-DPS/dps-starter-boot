package io.twdps.starter.boot.notifier.lifecycle.entity.provider;

import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;

import java.time.ZonedDateTime;

public class MemoizedTimestampProvider implements TimestampProvider {

  private ZonedDateTime ts;

  public MemoizedTimestampProvider(ZonedDateTime ts) {
    this.ts = ts;
  }

  public ZonedDateTime now() {
    return ts;
  }
}
