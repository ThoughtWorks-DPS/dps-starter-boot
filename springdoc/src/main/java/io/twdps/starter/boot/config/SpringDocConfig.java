package io.twdps.starter.boot.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

  /**
   * create configuration for public api docs.
   *
   * @return collection of api paths
   */
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("openapi-public").pathsToMatch("/v1/**").build();
  }

  /**
   * create configuration for admin api docs.
   *
   * @return collection of api paths
   */
  @Bean
  public GroupedOpenApi adminApi() {
    return GroupedOpenApi.builder().group("openapi-admin").pathsToMatch("/admin/v1/**").build();
  }

  /**
   * create configuration for internal api docs.
   *
   * @return collection of api paths
   */
  @Bean
  public GroupedOpenApi internalApi() {
    return GroupedOpenApi.builder().group("openapi-internal").pathsToMatch("/internal/v1/**").build();
  }
}
