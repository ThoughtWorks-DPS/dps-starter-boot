package io.twdps.starter.boot.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KafkaProducerTest {

  private BlockingQueue<ConsumerRecord<String, String>> records;

  private KafkaMessageListenerContainer<String, String> container;

  @Value("${test.topic}")
  private String topicName;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired
  private KafkaProducer producer;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeAll
  void setUp() {
    DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(
        getConsumerProperties());
    ContainerProperties containerProperties = new ContainerProperties(topicName);
    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    container.setupMessageListener((MessageListener<String, String>) records::add);
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
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  }

  @AfterAll
  void tearDown() {
    container.stop();
  }

//  @Disabled
  @Test
  void testWriteToKafka() throws InterruptedException, JsonProcessingException {
    // Create a user and write to Kafka
    String message = "Hello world!";
    producer.send(topicName, message);

    // Read the message (John Wick user) with a test consumer from Kafka and assert its properties
    ConsumerRecord<String, String> response = records.poll(5000, TimeUnit.MILLISECONDS);
    assertThat(response).isNotNull();
    assertThat(response.key()).isEqualTo("key");
    assertThat(response.value()).isEqualTo(message);
//    User result = objectMapper.readValue(response.value(), User.class);
//    assertNotNull(result);
//    assertEquals("John", result.getFirstName());
//    assertEquals("Wick", result.getLastName());
  }


}
