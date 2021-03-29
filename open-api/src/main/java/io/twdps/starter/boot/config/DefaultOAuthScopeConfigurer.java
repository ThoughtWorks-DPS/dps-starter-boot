package io.twdps.starter.boot.config;

import io.swagger.v3.oas.models.security.Scopes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "starter.openapi",
    name = "default-oauth-scopes",
    havingValue = "true",
    matchIfMissing = true)
public class DefaultOAuthScopeConfigurer implements CustomOAuthScopeConfigurer {

  @Override
  public void configure(Scopes scope) {
    scope.addString("read", "Read permissions");
    scope.addString("write", "Write permissions");
    scope.addString("delete", "Deletion permissions");
  }
}
