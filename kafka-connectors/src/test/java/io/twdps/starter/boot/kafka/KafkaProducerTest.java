package io.twdps.starter.boot.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(partitions = 1)
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KafkaProducerTest {

  private BlockingQueue<ConsumerRecord<Integer, TestMessage>> records;

  private KafkaMessageListenerContainer<Integer, TestMessage> container;

  @Value("${spring.kafka.topic.name}")
  private String topicName;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired
  private TestMessageKafkaProducer producer;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeAll
  void setUp() {
    DefaultKafkaConsumerFactory<Integer, TestMessage> consumerFactory =
        new DefaultKafkaConsumerFactory<>(getConsumerProperties());
    ContainerProperties containerProperties = new ContainerProperties(topicName);
    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    container.setupMessageListener((MessageListener<Integer, TestMessage>) records::add);
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
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
        JsonDeserializer.TRUSTED_PACKAGES, "io.twdps.starter.*");
  }

  @AfterAll
  void tearDown() {
    container.stop();
  }

  @Test
  void testWriteToKafka() throws InterruptedException, JsonProcessingException {
    // Create a user and write to Kafka
    String message = "Hello world!";
    TestMessage msg = new TestMessage(message);
    producer.send(topicName, msg);

    // Read the message (John Wick user) with a test consumer from Kafka and assert its properties
    ConsumerRecord<Integer, TestMessage> response = records.poll(5000, TimeUnit.MILLISECONDS);
    assertThat(response).isNotNull();
    assertThat(response.key()).isEqualTo(msg.getMessageIdentifier());
    assertThat(response.value()).isEqualTo(msg);
  }


}
