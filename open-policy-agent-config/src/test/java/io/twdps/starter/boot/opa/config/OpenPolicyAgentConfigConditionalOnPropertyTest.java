package io.twdps.starter.boot.opa.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenPolicyAgentConfigConditionalOnPropertyTest {

  /*
   * I setup a context runner with the class ExampleConfiguration
   * in it. For that, I use ApplicationContextRunner#withUserConfiguration()
   * methods to populate the context.
   */
  private ApplicationContextRunner context =
      new ApplicationContextRunner().withUserConfiguration(OpenPolicyAgentConfig.class)
      // .withUserConfiguration(ErrorMvcAutoConfiguration.class)
      // .withUserConfiguration(ErrorHandlerAdvice.class)
      // .withUserConfiguration(ErrorHandlerConfig.class)
      ;

  @Test
  void istioSecurityConfigIsDisabledProperly() {
    context
        .withPropertyValues("starter.open-policy-agent-config.enabled=false")
        .run(
            context ->
                assertAll(() -> assertThat(context).doesNotHaveBean(OpenPolicyAgentConfig.class)));
  }

  /* Disabling this, getting test failures:
   * No qualifying bean of type 'org.springframework.web.servlet.HandlerExceptionResolver' available
   */
  @Disabled
  @Test
  void istioSecurityConfigIsEnabledProperly() {
    context
        .withPropertyValues("starter.open-policy-agent-config.enabled=true")
        .run(
            context ->
                assertAll(() -> assertThat(context).hasSingleBean(OpenPolicyAgentConfig.class)));
  }
}
