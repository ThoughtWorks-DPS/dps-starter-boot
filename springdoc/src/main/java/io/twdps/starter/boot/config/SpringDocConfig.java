package io.twdps.starter.boot.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("openapi-public")
        .pathsToMatch("/v1/**")
        .build();
  }
  @Bean
  public GroupedOpenApi adminApi() {
    return GroupedOpenApi.builder()
        .group("openapi-admin")
        .pathsToMatch("/admin/**")
        .build();
  }
  @Bean
  public GroupedOpenApi internalApi() {
    return GroupedOpenApi.builder()
        .group("openapi-internal")
        .pathsToMatch("/internal/**")
        .build();
  }
}
