package io.twdps.starter.boot.notifier.lifecycle.entity.kafka.provider;

import static org.assertj.core.api.Assertions.assertThat;

import io.twdps.starter.boot.notifier.lifecycle.entity.kafka.config.properties.KafkaEntityLifecycleNotifierConfigProperties;
import io.twdps.starter.boot.notifier.lifecycle.entity.kafka.model.Foo;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityDescriptor;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityLifecycleNotification;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityLifecycleNotification.Operation;
import io.twdps.starter.boot.notifier.lifecycle.entity.provider.MemoizedTimestampProvider;
import io.twdps.starter.boot.notifier.lifecycle.entity.spi.TimestampProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@EmbeddedKafka(partitions = 1)
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KafkaEntityLifecycleNotifierTest {

  @TestConfiguration
  static class TestConfig {
    private final Logger log = LoggerFactory.getLogger(TestConfig.class);

    public ZonedDateTime now;

    public TestConfig() {
      log.warn("TestConfig constructor");
      this.now = ZonedDateTime.now();
    }

    @Bean
    public TimestampProvider timestampProvider() {
      log.warn("Configuring Timestamp Provider");
      return new MemoizedTimestampProvider(now);
    }
  }

  @Autowired private KafkaEntityTestConsumer consumer;

  @Autowired TimestampProvider ts;

  @Autowired KafkaEntityLifecycleNotifier notifier;

  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired private KafkaEntityLifecycleNotifierConfigProperties configProperties;

  private BlockingQueue<ConsumerRecord<Integer, String>> records;
  private KafkaMessageListenerContainer<Integer, String> container;

  private URI user = URI.create("user:uuid");
  private String version = "0.0.1";
  private Foo entity = new Foo("foo");
  private String typename = "io.twdps.starter.boot.notifier.lifecycle.entity.kafka.model.Foo";
  private ZonedDateTime now;
  private String nowStamp;

  EntityLifecycleNotification notification;

  @BeforeAll
  void setUp() {
    DefaultKafkaConsumerFactory<Integer, String> consumerFactory =
        new DefaultKafkaConsumerFactory<>(getConsumerProperties());
    ContainerProperties containerProperties =
        new ContainerProperties(configProperties.getTopic().getName());
    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    container.setupMessageListener((MessageListener<Integer, String>) records::add);
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
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    // JsonDeserializer.TRUSTED_PACKAGES, "io.twdps.starter.*");
  }

  @AfterAll
  void tearDown() {
    container.stop();
  }

  @BeforeEach
  public void setup() {
    this.now = ts.now();
    nowStamp = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    notification =
        EntityLifecycleNotification.builder()
            .timestamp(now)
            .actor(user)
            .version(version)
            .entityDescriptor(EntityDescriptor.builder().entity(entity).typename(typename).build())
            .operation(Operation.CREATED)
            .build();
    consumer.resetLatch();
  }

  private EntityLifecycleNotification from(EntityLifecycleNotification src, String version) {
    return EntityLifecycleNotification.builder()
        .timestamp(src.getTimestamp())
        .actor(src.getActor())
        .version(version)
        .entityDescriptor(src.getEntityDescriptor())
        .operation(src.getOperation())
        .build();
  }

  private void verify(Operation expectedOp, String expectedVersion) throws Exception {
    log.info("Waiting for message [{}:{}] to appear...", expectedOp, expectedVersion);

    // Show the raw message
    ConsumerRecord<Integer, String> response = records.poll(5000, TimeUnit.MILLISECONDS);
    String payload = response.value();
    log.info("Received [{}]", payload);

    consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

    assertThat(consumer.getLatch().getCount()).isEqualTo(0L);

    EntityLifecycleNotification obj = consumer.getPayload().getMessage();
    assertThat(obj).isNotNull();
    log.info("Received [{}]", obj);
    assertThat(obj.getVersion()).isEqualTo(expectedVersion);
    assertThat(obj.getOperation()).isEqualTo(expectedOp);
    assertThat(obj.getActor()).isEqualTo(user);
    assertThat(((Foo) obj.getEntityDescriptor().getEntity()).data).isEqualTo(entity.data);
    assertThat(obj.getTimestamp()).isEqualTo(now);
  }

  @Test
  public void notifyIsCalled() throws Exception {
    String v = "0.0.0";
    notifier.notify(from(notification, v));
    verify(Operation.CREATED, v);
  }

  @Test
  public void notifyIsCalledWhenCreated() throws Exception {
    String v = "0.1.0";
    notifier.created(entity, v, user);
    verify(Operation.CREATED, v);
  }

  @Test
  public void notifyIsCalledWhenUpdated() throws Exception {
    String v = "0.2.0";
    notifier.updated(entity, v, user);
    verify(Operation.UPDATED, v);
  }

  @Test
  public void notifyIsCalledWhenDeleted() throws Exception {
    String v = "0.3.0";
    notifier.deleted(entity, v, user);
    verify(Operation.DELETED, v);
  }

  @Test
  public void notifyIsCalledWhenCreatedLong() throws Exception {
    String v = "0.1.1";
    notifier.created(entity, entity.getClass(), v, user, now);
    verify(Operation.CREATED, v);
  }

  @Test
  public void notifyIsCalledWhenUpdatedLong() throws Exception {
    String v = "0.2.1";
    notifier.updated(entity, entity.getClass(), v, user, now);
    verify(Operation.UPDATED, v);
  }

  @Test
  public void notifyIsCalledWhenDeletedLong() throws Exception {
    String v = "0.3.1";
    notifier.deleted(entity, entity.getClass(), v, user, now);
    verify(Operation.DELETED, v);
  }
}
