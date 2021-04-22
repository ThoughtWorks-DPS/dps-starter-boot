package io.twdps.starter.boot.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

public class KafkaTestMessage extends TypedIdentifiableMessage<TestMessage> {

  public KafkaTestMessage(TestMessage message) {
    super(message);
  }

  @JsonCreator
  public KafkaTestMessage(
      @NonNull @JsonProperty("message") TestMessage message,
      @NonNull @JsonProperty("messageIdentifier") Integer messageIdentifier) {
    super(message, messageIdentifier);
  }
}
