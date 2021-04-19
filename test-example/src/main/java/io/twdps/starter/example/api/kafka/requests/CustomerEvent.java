package io.twdps.starter.example.api.kafka.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CustomerEvent {

  private int customerId;
  private String type;
  private long createdAt;

  /**
   *  constructor, for deserializing JSON objects.
   *
   * @param customerId eponymous
   * @param type eponymous
   * @param createdAt eponymous
   */
  @JsonCreator
  public CustomerEvent(@JsonProperty("customerId") int customerId,
      @NonNull @JsonProperty("type") String type,
      @JsonProperty("createdAt") long createdAt) {
    this.customerId = customerId;
    this.type = type;
    this.createdAt = createdAt;
  }

}
