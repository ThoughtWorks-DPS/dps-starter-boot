package io.twdps.starter.boot.notifier.lifecycle.entity.spi;

import java.time.ZonedDateTime;

public interface TimestampProvider {

  ZonedDateTime now();
}
