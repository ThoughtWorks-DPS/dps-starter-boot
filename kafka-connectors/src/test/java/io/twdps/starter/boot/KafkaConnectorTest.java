package io.twdps.starter.boot;

import io.twdps.starter.boot.kafka.KafkaTestMessage;
import io.twdps.starter.boot.kafka.TestMessage;
import io.twdps.starter.boot.kafka.TestMessageKafkaConsumer;
import io.twdps.starter.boot.kafka.TestMessageKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

//@EmbeddedKafka(
//    topics = "embedded-test-topic",
//    bootstrapServersProperty = "spring.kafka.bootstrap-servers",
//    partitions = 1,
//    controlledShutdown = false,
//    brokerProperties = {
//        "listeners=PLAINTEXT://localhost:9092",
//        "port=9092"
//    }
//)
@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KafkaConnectorTest {

  @Autowired
  private TestMessageKafkaConsumer consumer;

  @Autowired
  private TestMessageKafkaProducer producer;

  @Value("${spring.kafka.topic.name}")
  private String topicName;

  private String text = "Sending with simple KafkaProducer";
  private ZonedDateTime ts = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
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
    this.consumer.getLatch()
        .await(20000, TimeUnit.MILLISECONDS);

    assertThat(this.consumer.getLatch().getCount()).isEqualTo(0L);
    assertThat(this.consumer.getPayload().getMessage().getText()).isEqualTo(this.text);
    assertThat(this.consumer.getPayload()).isEqualTo(msg);
  }


}
