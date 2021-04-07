package io.twdps.starter.boot.notifier;

import java.time.ZonedDateTime;

public interface TimestampProvider {

  ZonedDateTime now();

}
