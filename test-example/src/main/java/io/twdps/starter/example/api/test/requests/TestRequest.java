package io.twdps.starter.example.api.test.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestRequest {

  @NonNull private final String userName;

  /**
   * Create object from json.
   *
   * @param userName username of Account holder
   */
  @JsonCreator
  public TestRequest(@NonNull @JsonProperty("userName") String userName) {

    this.userName = userName;
  }
}
