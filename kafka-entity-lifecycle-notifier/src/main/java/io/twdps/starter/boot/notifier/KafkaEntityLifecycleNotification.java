package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.twdps.starter.boot.kafka.TypedIdentifiableMessage;
import lombok.NonNull;

public class KafkaEntityLifecycleNotification extends
    TypedIdentifiableMessage<EntityLifecycleNotification> {

  public KafkaEntityLifecycleNotification(EntityLifecycleNotification message) {
    super(message);
  }

  @JsonCreator
  public KafkaEntityLifecycleNotification(
      @NonNull @JsonProperty("message") EntityLifecycleNotification message,
      @NonNull @JsonProperty("messageIdentifier") Integer messageIdentifier) {
    super(message, messageIdentifier);
  }
}
