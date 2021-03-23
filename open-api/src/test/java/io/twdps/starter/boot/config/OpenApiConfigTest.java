package io.twdps.starter.boot.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;


import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiConfigTest {

  /*
   * I setup a context runner with the class ExampleConfiguration
   * in it. For that, I use ApplicationContextRunner#withUserConfiguration()
   * methods to populate the context.
   */
  @Autowired
  ApplicationContextRunner context = new ApplicationContextRunner()
      .withUserConfiguration(OpenApiConfiguration.class);

  @Test
  public void openApiConfigurationBeanExists() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context.run(it -> {
      /*
       * I can use assertThat to assert on the context
       * and check if the @Bean configured is present
       * (and unique)
       */
      assertThat(it).hasSingleBean(OpenAPI.class);
    });
  }


}
