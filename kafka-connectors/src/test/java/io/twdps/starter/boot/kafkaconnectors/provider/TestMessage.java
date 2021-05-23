package io.twdps.starter.boot.kafkaconnectors.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class TestMessage {

  String text;
  ZonedDateTime timestamp;

  @JsonCreator
  public TestMessage(
      @NonNull @JsonProperty("text") String text,
      @NonNull @JsonProperty("timestamp") ZonedDateTime timestamp) {
    this.text = text;
    this.timestamp = timestamp;
  }
}
