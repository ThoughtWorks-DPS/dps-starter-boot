package io.twdps.starter.boot.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// https://github.com/zalando/problem-spring-web/tree/a3a45e57c7917f066474ef97a74d7307da1239e9/problem-spring-web


class IstioDisableSecurityConditionalOnPropertyTest {

  /*
  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
      // to print out conditional config report to log
      .withInitializer(new ConditionEvaluationReportLoggingListener())
      // .withUserConfiguration(ErrorHandlerAdvice.class, ErrorHandlerConfig.class)
      .withUserConfiguration(IstioDisableSecurityConfig.class);

  @Test
  void istioSecurityConfigIsDisabledProperly() {
    contextRunner
        .withPropertyValues("starter.istio-security-config=false")
        .run(context -> assertAll(
            () -> assertThat(context).doesNotHaveBean(IstioDisableSecurityConfig.class)));
  }

  @Test
  void istioSecurityConfigIsEnabledProperly() {
    contextRunner
        .withPropertyValues("starter.istio-security-config=true")
        .run(context -> assertAll(
            () -> assertThat(context).hasSingleBean(IstioDisableSecurityConfig.class)));
  }

     */
}
