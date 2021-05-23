package io.twdps.starter.boot.openapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.twdps.starter.boot.openapi.spi.CustomSchemaProvider;
import io.twdps.starter.boot.openapi.spi.CustomSecuritySchemeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@OpenAPIDefinition
@Configuration
@ConfigurationProperties(prefix = "starter.openapi")
public class OpenApiConfiguration {

  @Value("${license:MIT License}")
  private String license;

  @Value("${title:Example service}")
  private String title;

  @Value("${description:Example service providing Account info}")
  private String description;

  @Value("${version:v1}")
  private String version;

  @Value(
      "${licenseUrl:https://github.com/thoughtworks-dps/dps-multi-module-starterkit-java/blob/master/LICENSE}")
  private String licenseUrl;

  @Value("${contactEmail:FIXME@twdps.io}")
  private String contactEmail;

  @Value("${contactUrl:https://example.twdps.io/}")
  private String contactUrl;

  @Value("${contactName:Example}")
  private String contactName;

  @Value("${serverUrl:http://localhost:8080}")
  private String serverUrl;

  @Value("${oauthUrl:http://idp.twdps.io/}")
  private String oauthUrl;

  @Autowired(required = false)
  private List<CustomSchemaProvider> schemaProviders;

  @Autowired(required = false)
  private List<CustomSecuritySchemeProvider> securitySchemeProviders;

  /**
   * Configure OpenAPI processor.
   *
   * @return OpenAPI configuration bean
   */
  @Bean
  public OpenAPI customOpenApi() {

    OpenAPI config =
        new OpenAPI()
            .info(
                new io.swagger.v3.oas.models.info.Info()
                    .title(title)
                    .description(description)
                    .version(version)
                    .license(new License().name(license).url(licenseUrl))
                    .contact(new Contact().name(contactName).url(contactUrl).email(contactEmail)))
            .servers(Arrays.asList(new Server().url(serverUrl)))
            .addSecurityItem(
                new SecurityRequirement()
                    .addList("bearer-jwt", Arrays.asList("read", "write"))
                    .addList("oauth2", Arrays.asList("read", "write")));

    config = configureSecuritySchemes(config);
    config = configureSchemas(config);
    return config;
  }

  /**
   * Configure OpenAPI Security schemes.
   *
   * @param config current OpenAPI config object
   * @return OpenAPI config object
   */
  protected OpenAPI configureSecuritySchemes(OpenAPI config) {
    if (null != securitySchemeProviders && securitySchemeProviders.size() > 0) {
      Components securitySchemes = new Components();
      securitySchemeProviders.forEach(
          p -> {
            log.info("Adding SecurityScheme [{}]", p.getName());
            securitySchemes.addSecuritySchemes(p.getName(), p.create());
          });
      config.components(securitySchemes);
    } else {
      log.warn("No SecuritySchemeProviders defined.");
    }
    return config;
  }

  /**
   * Configure OpenAPI Schemas.
   *
   * @param config current OpenAPI config object
   * @return OpenAPI config object
   */
  protected OpenAPI configureSchemas(OpenAPI config) {
    if (null != schemaProviders && schemaProviders.size() > 0) {
      Components schemas = new Components();
      schemaProviders.stream()
          .forEach(
              p -> {
                log.info("Adding Schema [{}]", p.getName());
                schemas.addSchemas(p.getName(), p.create());
              });
      config.components(schemas);
    } else {
      log.info("No SchemaProviders defined.");
    }

    return config;
  }
}
