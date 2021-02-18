package io.twdps.starter.example.api.test.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
// @RequiredArgsConstructor
@Getter
public class TestResponse {

  //  @NonNull
  //  private final String id;
  @NonNull private final String userName;
}
