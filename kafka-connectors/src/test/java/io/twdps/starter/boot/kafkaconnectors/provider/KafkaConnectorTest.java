package io.twdps.starter.boot.kafkaconnectors.provider;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@EmbeddedKafka(partitions = 1)
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KafkaConnectorTest {

  @Autowired private TestMessageKafkaConsumer consumer;

  @Autowired private TestMessageKafkaProducer producer;

  @Value("${spring.kafka.topic.name}")
  private String topicName;

  private String text = "Sending with simple KafkaProducer";
  private ZonedDateTime ts = ZonedDateTime.now().withFixedOffsetZone();
  private TestMessage payload = TestMessage.builder().text(text).timestamp(ts).build();

  @BeforeEach
  public void setup() {
    this.consumer.resetLatch();
  }

  @Test
  public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived()
      throws Exception {
    assertThat(this.consumer).isNotNull();
    assertThat(this.producer).isNotNull();
    assertThat(this.consumer.getLatch().getCount()).isEqualTo(1L);

    KafkaTestMessage msg = new KafkaTestMessage(this.payload);
    this.producer.send(this.topicName, msg);
    this.consumer.getLatch().await(20000, TimeUnit.MILLISECONDS);

    assertThat(this.consumer.getLatch().getCount()).isEqualTo(0L);
    assertThat(this.consumer.getPayload().getMessage().getText()).isEqualTo(this.text);
    assertThat(this.consumer.getPayload()).isEqualTo(msg);
  }
}
