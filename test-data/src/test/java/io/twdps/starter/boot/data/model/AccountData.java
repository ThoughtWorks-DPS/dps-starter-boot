package io.twdps.starter.boot.data.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AccountData {

  private String id;
  private String userName;
  private String pii;
  private String firstName;
  private String lastName;
}
