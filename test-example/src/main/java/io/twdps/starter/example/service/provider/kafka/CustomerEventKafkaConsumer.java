package io.twdps.starter.example.service.provider.kafka;

import io.twdps.starter.boot.kafka.LatchedKafkaConsumer;
import io.twdps.starter.example.service.spi.kafka.model.CustomerEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CustomerEventKafkaConsumer extends LatchedKafkaConsumer<CustomerEventMessage> {

  public Logger getLogger() {
    return log;
  }

  /**
   * message handler.
   *
   * @param event customer event message
   */
  public void handleMessage(CustomerEventMessage event) {
    // process event message
    log.info("Received Message with event type:{}, customerID:{}",
        event.getType(), event.getCustomerId());
  }

}
