package io.twdps.starter.boot.config;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "starter.openapi",
    name = "default-oauth-security-scheme",
    havingValue = "true",
    matchIfMissing = true)
public class DefaultOAuthSecuritySchemeProvider implements CustomSecuritySchemeProvider {

  private String oauthUrl;
  private List<CustomOAuthScopeConfigurer> scopeConfigurers;

  @Autowired
  public DefaultOAuthSecuritySchemeProvider(
      @Value("${oauthUrl:http://idp.twdps.io/}")
          String oauthUrl, List<CustomOAuthScopeConfigurer> scopeConfigurers) {
    this.oauthUrl = oauthUrl;
    this.scopeConfigurers = scopeConfigurers;
  }

  /**
   * Create the SecurityScheme object for OpenAPI configuration bean.
   *
   * @return created SecurityScheme object
   */
  public SecurityScheme create() {

    Scopes scopes = new Scopes();
    if (null != scopeConfigurers) {
      scopeConfigurers.forEach(s -> s.configure(scopes));
    }

    return new SecurityScheme()
        .type(SecurityScheme.Type.OAUTH2)
        .flows(new OAuthFlows()
            .implicit(new OAuthFlow()
                .authorizationUrl(oauthUrl)
                .scopes(scopes)));

  }

  /**
   * return the name of the SecurityScheme object.
   *
   * @return name of the SecurityScheme configuration object
   */
  public String getName() {
    return "oauth2";
  }

}
