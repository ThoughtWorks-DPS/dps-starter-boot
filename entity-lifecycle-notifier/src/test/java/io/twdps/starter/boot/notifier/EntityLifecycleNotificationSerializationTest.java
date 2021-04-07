package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification.Operation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityLifecycleNotificationSerializationTest {

  private ObjectMapper mapper;

  String timeString = "2021-04-04T18:38:20.956276-04:00";
  ZonedDateTime timestamp = ZonedDateTime.parse(timeString, DateTimeFormatter.ISO_DATE_TIME);

  EntityLifecycleNotification notification = EntityLifecycleNotification.builder()
      .operation(Operation.CREATED)
      .actor(URI.create("user:uuid"))
      .timestamp(timestamp)
      .version("0.0.1")
      .entityDescriptor(EntityDescriptor.create(new Foo("foo")))
      .build();
  String json = "{\"version\":\"0.0.1\",\"operation\":\"CREATED\",\"timestamp\":\"2021-04-04T18:38:20.956276-04:00\",\"actor\":\"user:uuid\",\"entityDescriptor\":{\"typename\":\"io.twdps.starter.boot.notifier.Foo\",\"entity\":{\"data\":\"foo\"}}}";
  String typename = "io.twdps.starter.boot.notifier.Foo";


  @BeforeEach
  void configureObjectMapper() {
    mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addDeserializer(EntityDescriptor.class, new EntityDescriptorDeserializer());
    mapper.registerModule(module);

    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Test
  public void serializeTest() throws JsonProcessingException {
    String notificationJson = mapper.writeValueAsString(notification);
    assertThat(notificationJson).isEqualTo(json);
  }

  @Test
  public void deserializeTest() throws JsonProcessingException {
    EntityLifecycleNotification entity = mapper.readValue(json, EntityLifecycleNotification.class);
    assertThat(entity.getEntityDescriptor()
        .getTypename()).isEqualTo(typename);
    assertThat(entity.getEntityDescriptor()).isNotNull();
    assertThat(((Foo) entity.getEntityDescriptor()
        .getEntity()).data).isEqualTo("foo");
  }

}
