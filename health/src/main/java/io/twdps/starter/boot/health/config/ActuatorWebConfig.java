package io.twdps.starter.boot.health.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ActuatorWebConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/health").setViewName("/actuator/health");
    registry.addViewController("/health/liveness").setViewName("/actuator/health/liveness");
    registry.addViewController("/health/readiness").setViewName("/actuator/health/readiness");
    registry.addViewController("/liveness").setViewName("/actuator/health/liveness");
    registry.addViewController("/readiness").setViewName("/actuator/health/readiness");
  }
}
