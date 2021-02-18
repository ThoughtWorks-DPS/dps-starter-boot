package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

// https://github.com/zalando/problem-spring-web/tree/a3a45e57c7917f066474ef97a74d7307da1239e9/problem-spring-web

@Configuration
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class ErrorHandlerConfig {

  /**
   * Register bean for ObjectMapper to include conversions related to problem-spring-web.
   *
   * @return ObjectMapper instance bean
   */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModules(new ProblemModule(), new ConstraintViolationProblemModule());
  }
}
