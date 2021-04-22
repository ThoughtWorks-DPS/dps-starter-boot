package io.twdps.starter.example;

import io.twdps.starter.example.api.kafka.requests.CustomerEvent;
import io.twdps.starter.example.api.kafka.responses.EventKafkaMetadata;
import io.twdps.starter.example.service.provider.kafka.CustomerEventKafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

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
    eventConsumer.resetLatch();
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
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(eventConsumer.getLatch()
        .getCount()).isEqualTo(0);
    assertThat(response.getBody()
        .getCustomerId()).isEqualTo(customerEvent.getCustomerId());
    assertThat(response.getBody()
        .getOffset()).isGreaterThan(-1);
  }
}
