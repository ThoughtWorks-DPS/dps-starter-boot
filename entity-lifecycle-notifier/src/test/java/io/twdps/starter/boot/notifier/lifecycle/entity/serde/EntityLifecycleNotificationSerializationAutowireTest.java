package io.twdps.starter.boot.notifier.lifecycle.entity.serde;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.notifier.lifecycle.entity.config.EntityLifecycleNotifierConfig;
import io.twdps.starter.boot.notifier.lifecycle.entity.config.EntityLifecycleObjectMapperConfig;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityDescriptor;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityLifecycleNotification;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityLifecycleNotification.Operation;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.Foo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@ExtendWith(SpringExtension.class)
@JsonTest
@ContextConfiguration(
    classes = {EntityLifecycleNotifierConfig.class, EntityLifecycleObjectMapperConfig.class})
public class EntityLifecycleNotificationSerializationAutowireTest {

  @Autowired private ObjectMapper mapper;

  ZonedDateTime timestamp = ZonedDateTime.now();
  String timeString = timestamp.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

  EntityLifecycleNotification notification =
      EntityLifecycleNotification.builder()
          .operation(Operation.CREATED)
          .actor(URI.create("user:uuid"))
          .timestamp(timestamp)
          .version("0.0.1")
          .entityDescriptor(EntityDescriptor.create(new Foo("foo")))
          .build();
  String json =
      "{\"version\":\"0.0.1\","
          + "\"operation\":\"CREATED\","
          + "\"timestamp\":\""
          + timeString
          + "\","
          + "\"actor\":\"user:uuid\","
          + "\"entityDescriptor\":{\"typename\":"
          + "\"io.twdps.starter.boot.notifier.lifecycle.entity.model.Foo\","
          + "\"entity\":{\"data\":\"foo\"}}}";
  String typename = "io.twdps.starter.boot.notifier.lifecycle.entity.model.Foo";

  @Test
  public void serializeTest() throws JsonProcessingException {
    String notificationJson = mapper.writeValueAsString(notification);
    log.info("notification as json: [{}]", notificationJson);
    assertThat(notificationJson).isEqualTo(json);
    assertThat(notificationJson).contains(timeString);
  }

  @Test
  public void serializeTimestampTest() throws JsonProcessingException {
    String timestampJson = mapper.writeValueAsString(timestamp);
    log.info("timestamp as json: [{}]", timestampJson);
    log.info("timeString as json: [{}]", timeString);
    // We don't check for equality because mapper sticks the date inside quotes (")
    assertThat(timestampJson).contains(timeString);
    ZonedDateTime deserialized = mapper.readValue(timestampJson, ZonedDateTime.class);
    assertThat(deserialized).isEqualTo(timestamp);
    String timestampJson2 = mapper.writeValueAsString(deserialized);
    log.info("deserialized: [{}]", deserialized);
    log.info("timestamp2 as json: [{}]", timestampJson2);
    assertThat(timestampJson2).isEqualTo(timestampJson);
  }

  @Test
  public void deserializeTest() throws JsonProcessingException {
    EntityLifecycleNotification entity = mapper.readValue(json, EntityLifecycleNotification.class);
    assertThat(entity.getEntityDescriptor().getTypename()).isEqualTo(typename);
    assertThat(entity.getTimestamp()).isEqualTo(timestamp);
    assertThat(entity.getEntityDescriptor()).isNotNull();
    assertThat(((Foo) entity.getEntityDescriptor().getEntity()).data).isEqualTo("foo");
  }
}
