package io.twdps.starter.boot;

import io.twdps.starter.boot.kafka.CustomJsonSerializer;
import io.twdps.starter.boot.kafka.TestMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@JsonTest
class CustomJsonSerializerTest {

  private CustomJsonSerializer<TestMessage> serializer = new CustomJsonSerializer<>();

  private String text = "Sending with simple KafkaProducer";
  private ZonedDateTime now = ZonedDateTime.now();
  private String nowStamp = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  private TestMessage payload = TestMessage.builder().text(text).timestamp(now).build();

  @Test
  public void messageSerializeWithIsoFormat() throws Exception {

    byte[] bytes = serializer.serialize("topic-name", payload);
    String serde = new String(bytes);
    log.info("Serialized [{}]", serde);
    assertThat(serde).contains(nowStamp);
  }


}
