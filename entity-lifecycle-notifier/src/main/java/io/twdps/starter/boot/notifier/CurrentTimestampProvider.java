package io.twdps.starter.boot.notifier;

import java.time.ZonedDateTime;

public class CurrentTimestampProvider implements TimestampProvider {

  public ZonedDateTime now() {
    return ZonedDateTime.now();
  }

}
