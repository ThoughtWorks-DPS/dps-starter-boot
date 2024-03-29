package io.twdps.starter.boot.opa.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@ConditionalOnProperty(
    prefix = "starter.open-policy-agent-config",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class OpenPolicyAgentConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
  }
}
