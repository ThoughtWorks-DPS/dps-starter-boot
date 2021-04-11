package io.twdps.starter.boot.kafka;

import io.twdps.starter.boot.config.KafkaProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(classes = {KafkaProducerConfig.class, KafkaProducer.class, KafkaTestConsumer.class})
//@EnableKafka
//@EmbeddedKafka(
//    partitions = 1,
//    controlledShutdown = false,
//    brokerProperties = {
//        "listeners=PLAINTEXT://localhost:9092",
//        "port=9092"
//    })
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
//@ExtendWith(SpringExtension.class)
//@SpringJUnitConfig(classes = {KafkaProducerConfig.class, KafkaTestConsumer.class,
//    KafkaProducer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KafkaConnectorTest {

  @Autowired
  private KafkaTestConsumer consumer;

  @Autowired
  private KafkaProducer producer;

  @Value("${spring.kafka.topic.name}")
  private String topicName;
//  private String topic = "embedded-test-topic";
  private String payload = "Sending with simple KafkaProducer";

  @Test
  public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived()
      throws Exception {
    assertThat(consumer).isNotNull();
    assertThat(producer).isNotNull();

    producer.send(topicName, payload);
    consumer.getLatch()
        .await(20000, TimeUnit.MILLISECONDS);

    assertThat(consumer.getLatch()
        .getCount()).isEqualTo(0L);
    assertThat(consumer.getPayload()).isEqualTo(payload);
//    assertThat(consumer.getPayload()).containsPattern("embedded-test-topic");
  }


}
