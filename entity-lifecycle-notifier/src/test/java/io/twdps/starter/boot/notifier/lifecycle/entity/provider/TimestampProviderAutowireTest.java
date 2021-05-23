package io.twdps.starter.boot.notifier.lifecycle.entity.provider;

import static org.assertj.core.api.Assertions.assertThat;

import io.twdps.starter.boot.notifier.lifecycle.entity.config.EntityLifecycleNotifierConfig;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {TimestampProviderAutowireTest.TestConfig.class, EntityLifecycleNotifierConfig.class})
public class TimestampProviderAutowireTest {

  @TestConfiguration
  static class TestConfig {

    private String time = "2021-04-10T19:00:30.123456-04:00";
    private ZonedDateTime now = ZonedDateTime.parse(time);

    @Bean
    public TimestampProvider timestampProvider() {
      return new MemoizedTimestampProvider(now);
    }
  }

  private String time = "2021-04-10T19:00:30.123456-04:00";

  @Autowired TimestampProvider ts;

  @Test
  public void timestampProviderExists() {
    assertThat(ts).isNotNull();
    assertThat(ts.now()).isEqualTo(time);
    assertThat(ts).isInstanceOf(MemoizedTimestampProvider.class);
  }
}
