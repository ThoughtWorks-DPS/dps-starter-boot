package io.twdps.starter.boot.health.config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@ExtendWith(SpringExtension.class)
class ActuatorWebConfigTest {

  @Mock ViewControllerRegistry viewControllerRegistry;

  @Mock ViewControllerRegistration healthControllerRegistration;
  @Mock ViewControllerRegistration healthLivenessControllerRegistration;
  @Mock ViewControllerRegistration healthReadinessControllerRegistration;
  @Mock ViewControllerRegistration livenessControllerRegistration;
  @Mock ViewControllerRegistration readinessControllerRegistration;

  private final ActuatorWebConfig actuatorWebConfig = new ActuatorWebConfig();

  @Test
  void addsHealthViewNames() {
    when(viewControllerRegistry.addViewController("/health"))
        .thenReturn(healthControllerRegistration);
    when(viewControllerRegistry.addViewController("/health/liveness"))
        .thenReturn(healthLivenessControllerRegistration);
    when(viewControllerRegistry.addViewController("/health/readiness"))
        .thenReturn(healthReadinessControllerRegistration);
    when(viewControllerRegistry.addViewController("/liveness"))
        .thenReturn(livenessControllerRegistration);
    when(viewControllerRegistry.addViewController("/readiness"))
        .thenReturn(readinessControllerRegistration);

    this.actuatorWebConfig.addViewControllers(viewControllerRegistry);

    verify(healthControllerRegistration, times(1)).setViewName("/actuator/health");
    verify(healthLivenessControllerRegistration, times(1)).setViewName("/actuator/health/liveness");
    verify(healthReadinessControllerRegistration, times(1))
        .setViewName("/actuator/health/readiness");
    verify(livenessControllerRegistration, times(1)).setViewName("/actuator/health/liveness");
    verify(readinessControllerRegistration, times(1)).setViewName("/actuator/health/readiness");
  }
}
