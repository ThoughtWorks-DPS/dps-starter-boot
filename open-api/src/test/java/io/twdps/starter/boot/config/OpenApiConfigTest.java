package io.twdps.starter.boot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;


import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiConfigTest {

  /*
   * I setup a context runner with the class ExampleConfiguration
   * in it. For that, I use ApplicationContextRunner#withUserConfiguration()
   * methods to populate the context.
   */
  @Autowired
  ApplicationContextRunner context = new ApplicationContextRunner()
      .withUserConfiguration(DefaultOAuthScopeConfigurer.class)
      .withUserConfiguration(DefaultOAuthSecuritySchemeProvider.class)
      .withUserConfiguration(DefaultJwtBearerSecuritySchemeProvider.class)
      .withUserConfiguration(OpenApiConfiguration.class);

  @Test
  public void openApiConfigurationBeanExists() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context.run(it -> {
      /*
       * I can use assertThat to assert on the context
       * and check if the @Bean configured is present
       * (and unique)
       */
      assertThat(it).hasSingleBean(OpenAPI.class);
      OpenAPI config = it.getBean(OpenAPI.class);
      assertThat(config).isNotNull();
      assertThat(config.getComponents()
          .getSecuritySchemes()
          .containsKey("bearer-jwt")).isTrue();
    });
  }

  @Test
  public void openApiBearerJwtConditionalOnPropertyFalseNonexistent() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context
        .withPropertyValues("starter.openapi.default-jwt-bearer-security-scheme=false")
        .run(it -> {
          /*
           * I can use assertThat to assert on the context
           * and check if the @Bean configured is present
           * (and unique)
           */
          assertThat(it).hasSingleBean(OpenAPI.class);
          OpenAPI config = it.getBean(OpenAPI.class);
          assertThat(config).isNotNull();
          assertThat(config.getComponents()
              .getSecuritySchemes()
              .containsKey("bearer-jwt")).isFalse();
        });
  }

  @Test
  public void openApiOAuthConditionalOnPropertyFalseNonexistent() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context
        .withPropertyValues("starter.openapi.default-oauth-security-scheme=false")
        .run(it -> {
          /*
           * I can use assertThat to assert on the context
           * and check if the @Bean configured is present
           * (and unique)
           */
          assertThat(it).hasSingleBean(OpenAPI.class);
          OpenAPI config = it.getBean(OpenAPI.class);
          assertThat(config).isNotNull();
          assertThat(config.getComponents()
              .getSecuritySchemes()
              .containsKey("oauth2")).isFalse();
        });
  }

  @Test
  public void openApiOAuthScopesConditionalOnPropertyFalseNonexistent() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context
        .withPropertyValues("starter.openapi.default-oauth-scopes=false")
        .run(it -> {
          /*
           * I can use assertThat to assert on the context
           * and check if the @Bean configured is present
           * (and unique)
           */
          assertThat(it).hasSingleBean(OpenAPI.class);
          OpenAPI config = it.getBean(OpenAPI.class);
          assertThat(config).isNotNull();
          SecurityScheme oauth = config.getComponents()
              .getSecuritySchemes()
              .get("oauth2");
          assertThat(oauth.getFlows()
              .getImplicit()
              .getScopes()
              .containsKey("delete")).isFalse();
        });
  }


}
