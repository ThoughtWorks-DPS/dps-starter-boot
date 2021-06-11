package io.twdps.starter.example.api.kafka.resources;

import io.twdps.starter.example.api.kafka.requests.CustomerEvent;
import io.twdps.starter.example.api.kafka.responses.EventKafkaMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "/v1/events/customer", produces = "application/json")
public interface EventResource {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity<EventKafkaMetadata> createCustomerEventMessage(
      @RequestBody CustomerEvent customerEvent);
}
