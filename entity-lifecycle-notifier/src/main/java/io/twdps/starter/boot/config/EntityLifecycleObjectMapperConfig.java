package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.twdps.starter.boot.notifier.EntityDescriptor;
import io.twdps.starter.boot.notifier.EntityDescriptorDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityLifecycleObjectMapperConfig {
  private final Logger log = LoggerFactory.getLogger(EntityLifecycleObjectMapperConfig.class);

  /**
   * Configure ObjectMapper to serialize dates as strings. This autowired setter will configure an
   * existing ObjectMapper to (de)serialize dates as strings.
   *
   * @param mapper ObjectMapper bean to configure
   */
  @Autowired(required = true)
  public void configureObjectMapper(ObjectMapper mapper) {
    log.warn("Configuring Object Mapper");

    SimpleModule module = new SimpleModule();
    module.addDeserializer(EntityDescriptor.class, new EntityDescriptorDeserializer());
    mapper.registerModule(module);

    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

}
