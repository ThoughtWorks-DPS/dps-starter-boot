package io.twdps.starter.boot.notifier;

import io.twdps.starter.boot.config.EntityLifecycleObjectMapperConfig;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerializer<T> extends JsonSerializer<T> {

  /**
   * constructor for custom json serializer.
   * configure the ObjectMapper using the EntityLifecycleObjectMapperConfig function.
   */
  public CustomJsonSerializer() {
    super();
    // Use our standard configuration bean to configure the Kafka serializer
    EntityLifecycleObjectMapperConfig configurer = new EntityLifecycleObjectMapperConfig();
    configurer.configureObjectMapper(objectMapper);
  }
}
