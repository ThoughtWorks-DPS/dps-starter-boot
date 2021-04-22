package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.config.EntityLifecycleObjectMapperConfig;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification.Operation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EntityLifecycleNotificationSerializationTest {

  private ObjectMapper mapper;
  private EntityLifecycleObjectMapperConfig configurer = new EntityLifecycleObjectMapperConfig();

  String timeString = "2021-04-04T18:38:20.956276-04:00";
  ZonedDateTime timestamp = ZonedDateTime.parse(timeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

  EntityLifecycleNotification notification = EntityLifecycleNotification.builder()
      .operation(Operation.CREATED)
      .actor(URI.create("user:uuid"))
      .timestamp(timestamp)
      .version("0.0.1")
      .entityDescriptor(EntityDescriptor.create(new Foo("foo")))
      .build();
  String json = "{\"version\":\"0.0.1\","
      + "\"operation\":\"CREATED\","
      + "\"timestamp\":\"2021-04-04T18:38:20.956276-04:00\","
      + "\"actor\":\"user:uuid\","
      + "\"entityDescriptor\":{\"typename\":\"io.twdps.starter.boot.notifier.Foo\","
      + "\"entity\":{\"data\":\"foo\"}}}";
  String typename = "io.twdps.starter.boot.notifier.Foo";


  @BeforeEach
  void configureObjectMapper() {
    mapper = new ObjectMapper();

    configurer.configureObjectMapper(mapper);
  }

  @Test
  public void enumTest() {
    for (Operation op : Operation.values()) {
      Operation newOp = Operation.of(op.label);
      assertThat(newOp.label).isEqualTo(op.label);
    }
  }

  @Test
  public void serializeTest() throws JsonProcessingException {
    String notificationJson = mapper.writeValueAsString(notification);
    log.info("notification as json: [{}]", notificationJson);
    assertThat(notificationJson).isEqualTo(json);
  }

  @Test
  public void deserializeTest() throws JsonProcessingException {
    EntityLifecycleNotification entity = mapper.readValue(json, EntityLifecycleNotification.class);
    log.info("deserialized: [{}]", entity.toString()); // to obtain code coverage
    assertThat(entity.getEntityDescriptor()
        .getTypename()).isEqualTo(typename);
    assertThat(entity.getTimestamp()).isEqualTo(timeString);
    assertThat(entity.getEntityDescriptor()).isNotNull();
    assertThat(((Foo) entity.getEntityDescriptor()
        .getEntity()).data).isEqualTo("foo");
  }

}
