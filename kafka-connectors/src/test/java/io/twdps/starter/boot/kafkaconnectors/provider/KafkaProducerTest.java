package io.twdps.starter.boot.kafkaconnectors.provider;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.kafkaconnectors.config.properties.TestKafkaProducerConfigProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@EmbeddedKafka(partitions = 1)
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KafkaProducerTest {

  private BlockingQueue<ConsumerRecord<Integer, KafkaTestMessage>> records;

  private KafkaMessageListenerContainer<Integer, KafkaTestMessage> container;

  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired private TestMessageKafkaProducer producer;

  @Autowired private TestKafkaProducerConfigProperties configProperties;

  @Autowired private ObjectMapper objectMapper;

  private String text = "Sending with simple KafkaProducer";
  private ZonedDateTime ts = ZonedDateTime.now();
  private TestMessage payload = TestMessage.builder().text(text).timestamp(ts).build();

  @BeforeAll
  void setUp() {
    DefaultKafkaConsumerFactory<Integer, KafkaTestMessage> consumerFactory =
        new DefaultKafkaConsumerFactory<>(getConsumerProperties());
    ContainerProperties containerProperties =
        new ContainerProperties(configProperties.getTopic().getName());
    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    container.setupMessageListener((MessageListener<Integer, KafkaTestMessage>) records::add);
    container.start();
    ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
  }

  private Map<String, Object> getConsumerProperties() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
        ConsumerConfig.GROUP_ID_CONFIG, "consumer",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
        ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest",
        JsonDeserializer.TRUSTED_PACKAGES, "io.twdps.starter.*");
  }

  @AfterAll
  void tearDown() {
    container.stop();
  }

  @Test
  void testWriteToKafkaQueue() throws InterruptedException, JsonProcessingException {
    // Create a user and write to Kafka
    KafkaTestMessage msg = new KafkaTestMessage(payload);
    producer.send(configProperties.getTopic().getName(), msg);

    // Read the message (John Wick user) with a test consumer from Kafka and assert its properties
    ConsumerRecord<Integer, KafkaTestMessage> response = records.poll(5000, TimeUnit.MILLISECONDS);
    assertThat(response).isNotNull();
    assertThat(response.key()).isEqualTo(msg.getMessageIdentifier());
    assertThat(response.value().getMessage().getText()).isEqualTo(text);
    assertThat(response.value().getMessage().getTimestamp()).isEqualTo(ts);
  }

  @Disabled
  @Test
  void testWriteToKafka() throws InterruptedException, JsonProcessingException {
    // Create a user and write to Kafka
    KafkaTestMessage msg = new KafkaTestMessage(payload);
    producer.send(msg);

    // Read the message (John Wick user) with a test consumer from Kafka and assert its properties
    ConsumerRecord<Integer, KafkaTestMessage> response = records.poll(5000, TimeUnit.MILLISECONDS);
    assertThat(response).isNotNull();
    assertThat(response.key()).isEqualTo(msg.getMessageIdentifier());
    assertThat(response.value().getMessage().getText()).isEqualTo(text);
    assertThat(response.value().getMessage().getTimestamp()).isEqualTo(ts);
  }

  @Test
  void testFutureWriteToKafka()
      throws InterruptedException, JsonProcessingException, TimeoutException, ExecutionException {
    // Create a user and write to Kafka
    KafkaTestMessage msg = new KafkaTestMessage(payload);
    ListenableFuture<SendResult<Integer, KafkaTestMessage>> future = producer.sendMessage(msg);

    // Read the message (John Wick user) with a test consumer from Kafka and assert its properties
    SendResult<Integer, KafkaTestMessage> result = future.get(5000, TimeUnit.MILLISECONDS);
    assertThat(result).isNotNull();

    ConsumerRecord<Integer, KafkaTestMessage> response = records.poll(5000, TimeUnit.MILLISECONDS);
    assertThat(response).isNotNull();
    assertThat(response.key()).isEqualTo(msg.getMessageIdentifier());
    assertThat(response.value().getMessage().getText()).isEqualTo(text);
    assertThat(response.value().getMessage().getTimestamp()).isEqualTo(ts);
  }
}
