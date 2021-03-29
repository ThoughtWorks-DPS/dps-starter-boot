package io.twdps.starter.boot.config;

import io.swagger.v3.oas.models.security.SecurityScheme;

public interface CustomSecuritySchemeProvider {

  /**
   * Create the Schema object for OpenAPI configuration bean.
   *
   * @return created Schema object
   */
  SecurityScheme create();

  /**
   * return the name of the schema object.
   *
   * @return name of the schema object
   */
  String getName();

}
