package io.twdps.starter.example.service.spi.kafka;

import io.twdps.starter.example.api.kafka.requests.CustomerEvent;
import io.twdps.starter.example.api.kafka.responses.EventKafkaMetadata;

public interface EventProcessingService {


  /**
   * create message and send via kafka.
   *
   * @param customerEvent message to send
   * @return metadata record of kafka receipt
   */
  EventKafkaMetadata createAndSendEvent(CustomerEvent customerEvent);
}
