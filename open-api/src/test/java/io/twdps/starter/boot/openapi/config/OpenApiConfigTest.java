package io.twdps.starter.boot.openapi.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.twdps.starter.boot.openapi.provider.DefaultJwtBearerSecuritySchemeProvider;
import io.twdps.starter.boot.openapi.provider.DefaultOauthScopeConfigurer;
import io.twdps.starter.boot.openapi.provider.DefaultOauthSecuritySchemeProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

public class OpenApiConfigTest {

  /*
   * I setup a context runner with the class ExampleConfiguration
   * in it. For that, I use ApplicationContextRunner#withUserConfiguration()
   * methods to populate the context.
   */
  @Autowired
  ApplicationContextRunner context =
      new ApplicationContextRunner()
          .withUserConfiguration(DefaultOauthScopeConfigurer.class)
          .withUserConfiguration(DefaultOauthSecuritySchemeProvider.class)
          .withUserConfiguration(DefaultJwtBearerSecuritySchemeProvider.class)
          .withUserConfiguration(OpenApiConfiguration.class);

  @Test
  public void openApiConfigurationBeanExists() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context.run(
        it -> {
          /*
           * I can use assertThat to assert on the context
           * and check if the @Bean configured is present
           * (and unique)
           */
          assertThat(it).hasSingleBean(OpenAPI.class);
          OpenAPI config = it.getBean(OpenAPI.class);
          assertThat(config).isNotNull();
          assertThat(config.getComponents().getSecuritySchemes().containsKey("bearer-jwt"))
              .isTrue();
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
        .withPropertyValues("starter.openapi.default-jwt-bearer-security-scheme.enabled=false")
        .run(
            it -> {
              /*
               * I can use assertThat to assert on the context
               * and check if the @Bean configured is present
               * (and unique)
               */
              assertThat(it).hasSingleBean(OpenAPI.class);
              OpenAPI config = it.getBean(OpenAPI.class);
              assertThat(config).isNotNull();
              assertThat(config.getComponents().getSecuritySchemes().containsKey("bearer-jwt"))
                  .isFalse();
            });
  }

  @Test
  public void openApiOauthConditionalOnPropertyFalseNonexistent() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context
        .withPropertyValues("starter.openapi.default-oauth-security-scheme.enabled=false")
        .run(
            it -> {
              /*
               * I can use assertThat to assert on the context
               * and check if the @Bean configured is present
               * (and unique)
               */
              assertThat(it).hasSingleBean(OpenAPI.class);
              OpenAPI config = it.getBean(OpenAPI.class);
              assertThat(config).isNotNull();
              assertThat(config.getComponents().getSecuritySchemes().containsKey("oauth2"))
                  .isFalse();
            });
  }

  @Test
  public void openApiOauthScopesConditionalOnPropertyFalseNonexistent() {
    /*
     * We start the context and we will be able to trigger
     * assertions in a lambda receiving a
     * AssertableApplicationContext
     */
    context
        .withPropertyValues("starter.openapi.default-oauth-scopes.enabled=false")
        .run(
            it -> {
              /*
               * I can use assertThat to assert on the context
               * and check if the @Bean configured is present
               * (and unique)
               */
              assertThat(it).hasSingleBean(OpenAPI.class);
              OpenAPI config = it.getBean(OpenAPI.class);
              assertThat(config).isNotNull();
              SecurityScheme oauth = config.getComponents().getSecuritySchemes().get("oauth2");
              assertThat(oauth.getFlows().getImplicit().getScopes().containsKey("delete"))
                  .isFalse();
            });
  }
}
