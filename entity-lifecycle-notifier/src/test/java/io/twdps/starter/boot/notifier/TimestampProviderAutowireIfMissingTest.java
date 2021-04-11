package io.twdps.starter.boot.notifier;

import io.twdps.starter.boot.config.EntityLifecycleNotifierConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    EntityLifecycleNotifierConfig.class
})
public class TimestampProviderAutowireIfMissingTest {

  private String time = "2021-04-10T19:00:30.123456-04:00";
  @Autowired
  TimestampProvider ts;

  @Test
  public void timestampProviderExists() {
    assertThat(ts).isNotNull();
    ZonedDateTime now = ts.now();
    assertThat(ts.now()).isNotNull();
    assertThat(ts.now()).isNotEqualTo(time);
    assertThat(ts.now()).isNotEqualTo(now);
    assertThat(ts).isInstanceOf(CurrentTimestampProvider.class);
  }

}
