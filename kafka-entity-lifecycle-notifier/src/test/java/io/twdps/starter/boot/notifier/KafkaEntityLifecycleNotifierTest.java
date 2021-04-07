package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.twdps.starter.boot.config.KafkaEntityLifecycleNotifierConfig;
import io.twdps.starter.boot.kafka.KafkaProducer;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka
@SpringBootTest(
//    classes = {KafkaEntityLifecycleNotifierConfig.class},
    properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KafkaEntityLifecycleNotifierTest {

  private BlockingQueue<ConsumerRecord<String, String>> records;

  private KafkaMessageListenerContainer<String, String> container;

  @Autowired
  private KafkaEntityTestConsumer consumer;

  @Autowired
  private KafkaTemplate<String, EntityLifecycleNotification> producer;

  @Autowired
  private ObjectMapper mapper;

  @Value("${test.topic}")
  private String topic;

  private ZonedDateTime now = ZonedDateTime.now();
  private URI user = URI.create("user:uuid");
  private String version = "0.0.1";
  private Foo entity = new Foo("foo");
  private String typename = "io.twdps.starter.boot.notifier.Foo";
  private String queueName = "test-queue";

//  @Autowired
  KafkaEntityLifecycleNotifier notifier;

  @Autowired
  private KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate;

  public KafkaEntityLifecycleNotifier lifecycleNotifier() {
    return new KafkaEntityLifecycleNotifier(kafkaTemplate, new CurrentTimestampProvider(),
        topic);
  }


  EntityLifecycleNotification notification = EntityLifecycleNotification.builder()
      .timestamp(now)
      .actor(user)
      .version(version)
      .entityDescriptor(EntityDescriptor.builder()
          .entity(entity)
          .typename(typename)
          .build())
      .operation(Operation.CREATED)
      .build();


//  @Value("${test.topic}")
//  private String topicName;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  /*
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

*/

  void configureObjectMapper() {
    mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addDeserializer(EntityDescriptor.class, new EntityDescriptorDeserializer());
    mapper.registerModule(module);

    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @BeforeEach
  public void setup() {
    notifier = lifecycleNotifier();
    configureObjectMapper();
  }

 /*  */

  private void verify() throws Exception {
    consumer.getLatch()
        .await(10000, TimeUnit.MILLISECONDS);

    assertThat(consumer.getLatch()
        .getCount()).isEqualTo(0L);
//    assertThat(consumer.getPayload()).containsPattern("embedded-test-topic");

//    EntityLifecycleNotification obj = objectMapper.convertValue(consumer.getPayload(),
//        EntityLifecycleNotification.class);
    EntityLifecycleNotification obj = consumer.getPayload();
    assertThat(obj).isNotNull();
    assertThat(obj.getOperation()).isEqualTo(Operation.CREATED);

    /*
    assertThat(obj.getActor()).isEqualTo(user);
    assertThat(obj.getEntity()).isEqualTo(entity);
    assertThat(obj.getTimestamp()).isEqualTo(now);
    assertThat(obj.getVersion()).isEqualTo(version);
    */
  }

  @Test
  public void notifyIsCalled() throws Exception {
    notifier.notify(notification);
    verify();
  }

  @Disabled
  @Test
  public void notifyIsCalledWhenCreated() throws Exception {
    notifier.created(entity, version, user);
    verify();
  }

  @Disabled
  @Test
  public void notifyIsCalledWhenUpdated() throws Exception {
    notifier.updated(entity, version, user);
    verify();
  }

  @Disabled
  @Test
  public void notifyIsCalledWhenDeleted() throws Exception {
    notifier.deleted(entity, version, user);
    verify();
  }

  @Disabled
  @Test
  public void notifyIsCalledWhenCreatedLong() throws Exception {
    notifier.created(entity, entity.getClass(), version, user, now);
    verify();
  }

  @Disabled
  @Test
  public void notifyIsCalledWhenUpdatedLong() throws Exception {
    notifier.updated(entity, entity.getClass(), version, user, now);
    verify();
  }

  @Disabled
  @Test
  public void notifyIsCalledWhenDeletedLong() throws Exception {
    notifier.deleted(entity, entity.getClass(), version, user, now);
    verify();
  }

}
