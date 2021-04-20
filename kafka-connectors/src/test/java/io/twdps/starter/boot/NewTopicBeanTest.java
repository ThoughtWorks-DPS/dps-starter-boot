package io.twdps.starter.boot;

import io.twdps.starter.boot.config.SpringKafkaProducerConfigProperties;
import io.twdps.starter.boot.config.TestKafkaProducerConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class NewTopicBeanTest {

  @Autowired
  private List<NewTopic> newTopics;

  @Test
  public void allNewTopicBeansCreated() {
    newTopics.stream().forEach(t -> log.info("Topic [{}]", t.toString()));
    assertThat(newTopics).isNotNull();
    assertThat(newTopics).isNotEmpty();
    assertThat(newTopics.size()).isEqualTo(2);
  }

}
