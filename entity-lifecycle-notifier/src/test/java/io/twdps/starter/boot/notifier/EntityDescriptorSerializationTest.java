package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityDescriptorSerializationTest {

  private ObjectMapper mapper = new ObjectMapper();

  EntityDescriptor wrapper = EntityDescriptor.create(new Foo("foo"));
  String json = "{\"typename\":\"io.twdps.starter.boot.notifier.Foo\",\"entity\":{\"data\":\"foo\"}}";
  String fooJson = "{\"data\":\"foo\"}";
  String typename = "io.twdps.starter.boot.notifier.Foo";


  @Test
  public void serializeTest() throws JsonProcessingException {
    String wrapperJson = mapper.writeValueAsString(wrapper);
    assertThat(wrapperJson).isEqualTo(json);
  }

  @Test
  public void deserializeTest() throws JsonProcessingException {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(EntityDescriptor.class, new EntityDescriptorDeserializer());
    mapper.registerModule(module);

    EntityDescriptor entity = mapper.readValue(json, EntityDescriptor.class);
    assertThat(entity.getTypename()).isEqualTo(typename);
    assertThat(entity.getEntity()).isNotNull();
    assertThat(((Foo) entity.getEntity()).data).isEqualTo("foo");
  }

  @Test
  public void deserializeFooTest() throws JsonProcessingException {
    Foo entity = mapper.readValue(fooJson, Foo.class);
    assertThat(entity).isNotNull();
    assertThat(entity.data).isEqualTo("foo");
  }

}
