package io.twdps.starter.example.service.spi.kafka.model;

import io.twdps.starter.boot.kafka.IdentifiableMessage;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CustomerEventMessage implements IdentifiableMessage {

  private int eventId;
  private int customerId;
  private long createdAt;
  private long lastModified;
  private String type;

  @Override
  public Integer getMessageIdentifier() {
    return Integer.valueOf(getEventId());
  }

}
