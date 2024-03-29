package io.twdps.starter.boot.openapi.provider;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.twdps.starter.boot.openapi.spi.CustomSecuritySchemeProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    prefix = "starter.openapi.default-jwt-bearer-security-scheme",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class DefaultJwtBearerSecuritySchemeProvider implements CustomSecuritySchemeProvider {

  /**
   * Create the SecurityScheme object for OpenAPI configuration bean.
   *
   * @return created SecurityScheme object
   */
  public SecurityScheme create() {

    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(SecurityScheme.In.HEADER)
        .name("Authorization");
  }

  /**
   * return the name of the SecurityScheme object.
   *
   * @return name of the SecurityScheme configuration object
   */
  public String getName() {
    return "bearer-jwt";
  }
}
