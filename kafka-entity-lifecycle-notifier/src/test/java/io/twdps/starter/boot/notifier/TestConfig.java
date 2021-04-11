package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;

@TestConfiguration
public class TestConfig {
  private final Logger log = LoggerFactory.getLogger(TestConfig.class);

  public ZonedDateTime now;

  public TestConfig() {
    log.warn("TestConfig constructor");
    this.now = ZonedDateTime.now();
  }

  @Bean
  public TimestampProvider timestampProvider() {
    log.warn("Configuring Timestamp Provider");
    return new MemoizedTimestampProvider(now);
  }
  
}
