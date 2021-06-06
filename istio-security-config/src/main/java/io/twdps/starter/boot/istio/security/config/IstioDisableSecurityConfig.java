package io.twdps.starter.boot.istio.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ConditionalOnProperty(
    prefix = "starter.istio-security-config",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebSecurity
public class IstioDisableSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.authorizeRequests().antMatchers("/").permitAll();
  }
}
