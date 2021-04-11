package io.twdps.starter.boot.notifier;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;

//@TestConfiguration
public class TestConfig {

  private String time = "2021-04-10T19:00:30.123456-04:00";
  private ZonedDateTime now = ZonedDateTime.parse(time);

  @Bean
  public TimestampProvider timestampProvider() {
    return new MemoizedTimestampProvider(now);
  }
}
