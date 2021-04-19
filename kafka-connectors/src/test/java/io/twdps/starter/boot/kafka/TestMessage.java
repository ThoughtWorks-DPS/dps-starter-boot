package io.twdps.starter.boot.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@EqualsAndHashCode
@ToString
public class TestMessage implements IdentifiableMessage {

  private static AtomicInteger ident = new AtomicInteger(0);

  private final String message;
  private final Integer messageIdentifier;

  public TestMessage(String message) {
    this.message = message;
    this.messageIdentifier = ident.incrementAndGet();
  }

  @JsonCreator
  public TestMessage(
      @NonNull @JsonProperty("message") String message,
      @NonNull @JsonProperty("messageIdentifier") Integer messageIdentifier) {
    this.message = message;
    this.messageIdentifier = messageIdentifier;
  }
}
