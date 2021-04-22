package io.twdps.starter.boot.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.twdps.starter.boot.config.KafkaSerdeObjectMapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.lang.Nullable;

import java.io.IOException;

@Slf4j
public class CustomJsonSerializer<T> extends JsonSerializer<T> {

  private ObjectWriter myWriter;

  /**
   * constructor for custom json serializer. configure the ObjectMapper using the
   * KafkaSerdeObjectMapperConfig function.
   */
  public CustomJsonSerializer() {
    super();
    log.warn("Configuring Kafka object mapper [{}]", this.objectMapper);
    // Use our standard configuration bean to configure the Kafka serializer
    KafkaSerdeObjectMapperConfig configurer = new KafkaSerdeObjectMapperConfig();
    configurer.configureObjectMapper(objectMapper);
    myWriter = objectMapper.writerFor((JavaType) null);
  }

  @Override
  @Nullable
  public byte[] serialize(String topic, @Nullable T data) {
    if (data == null) {
      return null;
    }
    try {
      return this.myWriter.writeValueAsBytes(data);
    } catch (IOException ex) {
      throw new SerializationException(
          "Can't serialize data [" + data + "] for topic [" + topic + "]", ex);
    }
  }

}
