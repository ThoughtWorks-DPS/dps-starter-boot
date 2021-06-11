package io.twdps.starter.example.controller.kafka;

import io.twdps.starter.example.api.kafka.requests.CustomerEvent;
import io.twdps.starter.example.api.kafka.resources.EventResource;
import io.twdps.starter.example.api.kafka.responses.EventKafkaMetadata;
import io.twdps.starter.example.service.provider.kafka.EventProcessingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EventController implements EventResource {

  @Autowired private EventProcessingServiceImpl eventProcessingService;

  @Override
  public ResponseEntity<EventKafkaMetadata> createCustomerEventMessage(
      @RequestBody CustomerEvent customerEvent) {

    EventKafkaMetadata eventKafkaMetadata =
        eventProcessingService.createAndSendEvent(customerEvent);
    return new ResponseEntity<EventKafkaMetadata>(eventKafkaMetadata, HttpStatus.OK);
  }
}
