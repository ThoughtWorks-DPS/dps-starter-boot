package io.twdps.starter.boot.notifier.lifecycle.entity.serde;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.notifier.lifecycle.entity.config.EntityLifecycleNotifierConfig;
import io.twdps.starter.boot.notifier.lifecycle.entity.config.EntityLifecycleObjectMapperConfig;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityDescriptor;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.Foo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@JsonTest
@ContextConfiguration(
    classes = {EntityLifecycleNotifierConfig.class, EntityLifecycleObjectMapperConfig.class})
public class EntityDescriptorSerializationAutowireTest {

  @Autowired private ObjectMapper mapper;

  EntityDescriptor wrapper = EntityDescriptor.create(new Foo("foo"));
  String json =
      new StringBuilder()
          .append("{\"typename\":\"io.twdps.starter.boot.notifier.lifecycle.entity.model.Foo\",")
          .append("\"entity\":{\"data\":\"foo\"}}")
          .toString();
  String fooJson = "{\"data\":\"foo\"}";
  String typename = "io.twdps.starter.boot.notifier.lifecycle.entity.model.Foo";

  @Test
  public void serializeTest() throws JsonProcessingException {
    String wrapperJson = mapper.writeValueAsString(wrapper);
    log.info("wrapper as json: [{}]", wrapperJson);
    assertThat(wrapperJson).isEqualTo(json);
  }

  @Test
  public void deserializeTest() throws JsonProcessingException {
    EntityDescriptor entity = mapper.readValue(json, EntityDescriptor.class);
    assertThat(entity.getTypename()).isEqualTo(typename);
    assertThat(entity.getEntity()).isNotNull();
    assertThat(entity.getEntity()).isInstanceOf(Foo.class);
    assertThat(((Foo) entity.getEntity()).data).isEqualTo("foo");
  }

  @Test
  public void deserializeFooTest() throws JsonProcessingException {
    Foo entity = mapper.readValue(fooJson, Foo.class);
    assertThat(entity).isNotNull();
    assertThat(entity.data).isEqualTo("foo");
  }
}
