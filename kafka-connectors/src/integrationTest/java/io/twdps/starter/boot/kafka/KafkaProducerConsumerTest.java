package io.twdps.starter.boot.kafka;


import io.twdps.starter.example.api.kafka.requests.CustomerEvent;
import io.twdps.starter.example.api.kafka.responses.EventKafkaMetadata;
import io.twdps.starter.example.service.provider.kafka.CustomerEventKafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@ExtendWith(SpringExtension.class)
//@EmbeddedKafka(partitions = 1,topics = {"${spring.kafka.topic.name}"})
@EmbeddedKafka(partitions = 1)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
)
//@DirtiesContext
public class KafkaProducerConsumerTest {

  private CustomerEvent customerEvent = new CustomerEvent();

  private String url = "http://localhost";
  private String urlPath = "/v1/events/customer";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private CustomerEventKafkaConsumer eventConsumer;

  @BeforeEach
  private void setup() {

    customerEvent.setCreatedAt(ZonedDateTime.now()
        .toEpochSecond());
    customerEvent.setCustomerId(1000);
    customerEvent.setType("CUSTOMER_ADDED");
  }

  private String getRequestUrl() {
    return new StringBuilder(url)
        .append(":")
        .append(port)
        .append(urlPath)
        .toString();
  }

  @Test
  public void testReceive() throws Exception {

    HttpEntity<CustomerEvent> request = new HttpEntity<>(customerEvent);
    ResponseEntity<EventKafkaMetadata> response = this.restTemplate.postForEntity(
        getRequestUrl(), request, EventKafkaMetadata.class);
    eventConsumer.getLatch()
        .await(10000, TimeUnit.MILLISECONDS);
    System.out.println("response->" + response.getStatusCode());
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    assertEquals(eventConsumer.getLatch()
        .getCount(), 0);
    assertEquals(response.getBody()
        .getCustomerId(), customerEvent.getCustomerId());
    assertTrue(response.getBody()
        .getOffset() > -1);
  }
}
