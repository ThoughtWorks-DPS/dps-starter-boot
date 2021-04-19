package io.twdps.starter.boot.kafka;

import io.twdps.starter.boot.config.KafkaProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

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

  private String payload = "Sending with simple KafkaProducer";

  @Test
  public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived()
      throws Exception {
    assertThat(consumer).isNotNull();
    assertThat(producer).isNotNull();

    TestMessage msg = new TestMessage(payload);
    consumer.resetLatch();
    producer.send(topicName, msg);
    consumer.getLatch()
        .await(20000, TimeUnit.MILLISECONDS);

    assertThat(consumer.getLatch()
        .getCount()).isEqualTo(0L);
    assertThat(consumer.getPayload()).isEqualTo(msg);
  }


}
