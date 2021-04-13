package io.twdps.starter.boot.notifier;

import io.twdps.starter.boot.notifier.EntityLifecycleNotification.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@EmbeddedKafka
@SpringBootTest(
    properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "starter.boot.kafka-lifecycle-notifier.queue-name=raw-queue-name",
        // CSOFF: LineLength
        "spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer"
        // CSON: LineLength
    })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class KafkaEntityLifecycleNotifierSerializerTest {

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

  private BlockingQueue<ConsumerRecord<String, String>> records;

  private KafkaMessageListenerContainer<String, String> container;

  @Autowired
  private KafkaRawTestConsumer consumer;

  @Autowired
  private KafkaTemplate<String, EntityLifecycleNotification> kafkaTemplate;

  @Autowired
  TimestampProvider ts;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired
  KafkaEntityLifecycleNotifier notifier;


  private URI user = URI.create("user:uuid");
  private String version = "0.0.1";
  private Foo entity = new Foo("foo");
  private String typename = "io.twdps.starter.boot.notifier.Foo";
  private ZonedDateTime now;


  EntityLifecycleNotification notification;

  @BeforeEach
  public void setup() {
    this.now = ts.now();
    notification = EntityLifecycleNotification.builder()
        .timestamp(now)
        .actor(user)
        .version(version)
        .entityDescriptor(EntityDescriptor.builder()
            .entity(entity)
            .typename(typename)
            .build())
        .operation(Operation.CREATED)
        .build();
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
    consumer.getLatch()
        .await(10000, TimeUnit.MILLISECONDS);

    assertThat(consumer.getLatch()
        .getCount()).isEqualTo(0L);

    String payload = consumer.getPayload();
    assertThat(payload).isNotNull();
    log.info("Received [{}]", payload);
    assertThat(payload).contains(expectedVersion);
    assertThat(payload).contains(expectedOp.label);
    assertThat(payload).contains(user.toString());
    assertThat(payload).contains(entity.data);
    assertThat(payload).contains(now.toString());
  }

  @Test
  public void notifyIsCalled() throws Exception {
    String v = "0.0.0";
    consumer.resetLatch();
    notifier.notify(from(notification, v));
    verify(Operation.CREATED, v);
  }

  @Test
  public void notifyIsCalledWhenCreated() throws Exception {
    String v = "0.1.0";
    consumer.resetLatch();
    notifier.created(entity, v, user);
    verify(Operation.CREATED, v);
  }

  @Test
  public void notifyIsCalledWhenUpdated() throws Exception {
    String v = "0.2.0";
    consumer.resetLatch();
    notifier.updated(entity, v, user);
    verify(Operation.UPDATED, v);
  }

  @Test
  public void notifyIsCalledWhenDeleted() throws Exception {
    String v = "0.3.0";
    consumer.resetLatch();
    notifier.deleted(entity, v, user);
    verify(Operation.DELETED, v);
  }

  @Test
  public void notifyIsCalledWhenCreatedLong() throws Exception {
    String v = "0.1.1";
    consumer.resetLatch();
    notifier.created(entity, entity.getClass(), v, user, now);
    verify(Operation.CREATED, v);
  }

  @Test
  public void notifyIsCalledWhenUpdatedLong() throws Exception {
    String v = "0.2.1";
    consumer.resetLatch();
    notifier.updated(entity, entity.getClass(), v, user, now);
    verify(Operation.UPDATED, v);
  }

  @Test
  public void notifyIsCalledWhenDeletedLong() throws Exception {
    String v = "0.3.1";
    consumer.resetLatch();
    notifier.deleted(entity, entity.getClass(), v, user, now);
    verify(Operation.DELETED, v);
  }

}
