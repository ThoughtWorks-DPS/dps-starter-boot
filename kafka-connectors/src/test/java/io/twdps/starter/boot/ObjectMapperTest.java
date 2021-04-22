package io.twdps.starter.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.config.KafkaSerdeObjectMapperConfig;
import io.twdps.starter.boot.kafka.TestMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@JsonTest
@ContextConfiguration(classes = {
    KafkaSerdeObjectMapperConfig.class
})
class ObjectMapperTest {

  @Autowired
  private ObjectMapper mapper;

  private String text = "Sending with simple KafkaProducer";
  private ZonedDateTime now = ZonedDateTime.now();
  private String nowStamp = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  private TestMessage payload = TestMessage.builder().text(text).timestamp(now).build();

  @Test
  public void messageSerializeWithIsoFormat() throws Exception {

    String serde = mapper.writeValueAsString(payload);
    log.info("payload serde [{}]", serde);
    assertThat(serde).contains(nowStamp);
  }

  @Test
  public void timestampSerializeWithIsoFormat() throws Exception {

    String serde = mapper.writeValueAsString(now);
    assertThat(serde).contains(nowStamp);

    ZonedDateTime deserialized = mapper.readValue(serde, ZonedDateTime.class);
    assertThat(deserialized).isEqualTo(now);

    String deser = mapper.writeValueAsString(deserialized);
    log.info("round trip: [{}]", serde, deser);
    assertThat(deser).isEqualTo(serde);
  }


}
