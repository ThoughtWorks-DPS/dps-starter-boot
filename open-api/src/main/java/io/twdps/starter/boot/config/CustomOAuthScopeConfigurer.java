package io.twdps.starter.boot.config;


import io.swagger.v3.oas.models.security.Scopes;

public interface CustomOAuthScopeConfigurer {

  /**
   * Configure the Scope object for OpenAPI configuration bean.
   */
  void configure(Scopes scope);

}
