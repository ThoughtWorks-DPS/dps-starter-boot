package io.twdps.starter.example.service.spi.test.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class TestModel {

  @NonNull private final String userName;
}
