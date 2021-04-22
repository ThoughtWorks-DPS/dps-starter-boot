package io.twdps.starter.boot.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TypedIdentifiableMessage<T> implements IdentifiableMessage {

  private static AtomicInteger ident = new AtomicInteger(0);

  private final T message;
  private final Integer messageIdentifier;

  public TypedIdentifiableMessage(T message) {
    this.message = message;
    this.messageIdentifier = ident.incrementAndGet();
  }
}
