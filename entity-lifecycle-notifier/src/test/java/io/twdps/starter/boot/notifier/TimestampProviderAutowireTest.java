package io.twdps.starter.boot.notifier;

import io.twdps.starter.boot.config.EntityLifecycleNotifierConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    TestConfig.class,
    EntityLifecycleNotifierConfig.class
})
public class TimestampProviderAutowireTest {

  private String time = "2021-04-10T19:00:30.123456-04:00";
  @Autowired
  TimestampProvider ts;

  @Test
  public void timestampProviderExists() {
    assertThat(ts).isNotNull();
    assertThat(ts.now()).isEqualTo(time);
  }

}
