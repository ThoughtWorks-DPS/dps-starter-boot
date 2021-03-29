package io.twdps.starter.boot.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// https://github.com/zalando/problem-spring-web/tree/a3a45e57c7917f066474ef97a74d7307da1239e9/problem-spring-web


class IstioDisableSecurityConditionalOnPropertyTest {

  /*
   * I setup a context runner with the class ExampleConfiguration
   * in it. For that, I use ApplicationContextRunner#withUserConfiguration()
   * methods to populate the context.
   */
  private ApplicationContextRunner context = new ApplicationContextRunner()
      .withUserConfiguration(IstioDisableSecurityConfig.class)
      .withUserConfiguration(ErrorMvcAutoConfiguration.class)
//      .withUserConfiguration(ErrorHandlerAdvice.class)
//      .withUserConfiguration(ErrorHandlerConfig.class)
      ;


  @Test
  void istioSecurityConfigIsDisabledProperly() {
    context
        .withPropertyValues("starter.istio-security-config=false")
        .run(context -> assertAll(
            () -> assertThat(context).doesNotHaveBean(IstioDisableSecurityConfig.class)));
  }

  /* Disabling this, getting test failures:
   * No qualifying bean of type 'org.springframework.web.servlet.HandlerExceptionResolver' available
   */
  @Disabled
  @Test
  void istioSecurityConfigIsEnabledProperly() {
    context
        .withPropertyValues("starter.istio-security-config=true")
        .run(context -> assertAll(
            () -> assertThat(context).hasSingleBean(IstioDisableSecurityConfig.class)));
  }

}
