package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

// https://github.com/zalando/problem-spring-web/tree/a3a45e57c7917f066474ef97a74d7307da1239e9/problem-spring-web

@Configuration
public class ErrorHandlerConfig {

  /**
   * Configure ObjectMapper bean to include conversions related to problem-spring-web.
   *
   * @param objectMapper ObjectMapper instance bean
   */
  @Autowired(required = true)
  public void configureObjectMapper(ObjectMapper objectMapper) {
    objectMapper.registerModules(new ProblemModule(), new ConstraintViolationProblemModule());
  }

}
