package io.twdps.starter.boot.kafka;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestMessageKafkaConsumer extends LatchedKafkaConsumer<TestMessage> {

  public Logger getLogger() {
    return log;
  }
}
