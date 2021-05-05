package io.twdps.starter.example.service.spi.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
@AllArgsConstructor
public class TestModel {

  private String id;

  @NonNull private final String userName;
}
