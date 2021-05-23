package io.twdps.starter.boot.notifier.lifecycle.entity.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.twdps.starter.boot.notifier.lifecycle.entity.model.EntityDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class EntityDescriptorDeserializer extends StdDeserializer<EntityDescriptor> {

  public EntityDescriptorDeserializer() {
    this(null);
  }

  public EntityDescriptorDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public EntityDescriptor deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    log.trace("Descriptor tree: [{}]", node);

    String typename = node.get("typename").asText();
    log.trace("Deserializing type: [{}]", typename);

    JsonNode objNode = node.get("entity");
    log.trace("From tree: [{}]", objNode);

    JsonParser objParser = objNode.traverse(jp.getCodec());
    objParser.nextToken(); // Need to do this to advance the parser

    try {
      Class<? extends Object> objClass = Class.forName(typename);
      log.trace("Found decoder class [{}]", objClass.getCanonicalName());
      Object obj = objParser.readValueAs(objClass);

      return EntityDescriptor.builder().typename(typename).entity(obj).build();
    } catch (ClassNotFoundException ex) {
      log.error("Unable to deserialize for class [{}]", typename, ex);
      return null;
    }
  }
}
