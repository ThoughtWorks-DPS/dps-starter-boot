package io.twdps.starter.boot.kafkaconnectors.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.kafkaconnectors.config.KafkaSerdeObjectMapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Slf4j
public class CustomJsonDeserializer<T> extends JsonDeserializer<T> {

  public CustomJsonDeserializer() {
    super(customizedObjectMapper());
  }

  private static ObjectMapper customizedObjectMapper() {
    ObjectMapper mapper = JacksonUtils.enhancedObjectMapper();
    KafkaSerdeObjectMapperConfig configurer = new KafkaSerdeObjectMapperConfig();
    configurer.configureObjectMapper(mapper);
    return mapper;
  }
}
