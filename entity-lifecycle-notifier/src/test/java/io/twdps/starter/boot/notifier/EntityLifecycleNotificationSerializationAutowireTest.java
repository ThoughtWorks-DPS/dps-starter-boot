package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.config.EntityLifecycleNotifierConfig;
import io.twdps.starter.boot.config.EntityLifecycleObjectMapperConfig;
import io.twdps.starter.boot.notifier.EntityLifecycleNotification.Operation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
@ContextConfiguration(classes = {
    EntityLifecycleNotifierConfig.class,
    EntityLifecycleObjectMapperConfig.class
})
public class EntityLifecycleNotificationSerializationAutowireTest {

  @Autowired
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
  String json = "{\"version\":\"0.0.1\","
      + "\"operation\":\"CREATED\","
      + "\"timestamp\":\"2021-04-04T18:38:20.956276-04:00\","
      + "\"actor\":\"user:uuid\","
      + "\"entityDescriptor\":{\"typename\":\"io.twdps.starter.boot.notifier.Foo\","
      + "\"entity\":{\"data\":\"foo\"}}}";
  String typename = "io.twdps.starter.boot.notifier.Foo";


  @Test
  public void serializeTest() throws JsonProcessingException {
    String notificationJson = mapper.writeValueAsString(notification);
    assertThat(notificationJson).isEqualTo(json);
    assertThat(notificationJson).contains(timeString);
  }

  @Test
  public void deserializeTest() throws JsonProcessingException {
    EntityLifecycleNotification entity = mapper.readValue(json, EntityLifecycleNotification.class);
    assertThat(entity.getEntityDescriptor()
        .getTypename()).isEqualTo(typename);
    assertThat(entity.getTimestamp()).isEqualTo(timeString);
    assertThat(entity.getEntityDescriptor()).isNotNull();
    assertThat(((Foo) entity.getEntityDescriptor()
        .getEntity()).data).isEqualTo("foo");
  }

}
