package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(name = {"io.twdps.starter.boot.config.EntityLifecycleNotifierConfig"})
public class KafkaSerdeObjectMapperConfig {
  private final Logger log = LoggerFactory.getLogger(KafkaSerdeObjectMapperConfig.class);

  /**
   * Configure ObjectMapper to serialize dates as strings. This autowired setter will configure an
   * existing ObjectMapper to (de)serialize dates as strings.
   *
   * @param mapper ObjectMapper bean to configure
   */
  @Autowired(required = true)
  public void configureObjectMapper(ObjectMapper mapper) {
    log.warn("Configuring (spring) Object Mapper [{}]", mapper);

    mapper.registerModule(new JavaTimeModule());
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

}
