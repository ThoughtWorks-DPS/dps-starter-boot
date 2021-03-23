package io.twdps.starter.boot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

@OpenAPIDefinition
@SecurityScheme(
    name = "oauth2",
    scheme = "OAuth2",
    type = SecuritySchemeType.OAUTH2,
    in = SecuritySchemeIn.HEADER)
@Configuration
public class OpenApiConfiguration {

  @Value("${starter.openapi.license:MIT License}")
  private String license;

  @Value("${starter.openapi.title:Example service}")
  private String title;

  @Value("${starter.openapi.description:Example service providing Account info}")
  private String description;

  @Value("${starter.openapi.version:v1}")
  private String version;

  @Value(
      "${starter.openapi.licenseUrl:https://github.com/thoughtworks-dps/dps-multi-module-starterkit-java/blob/master/LICENSE}")
  private String licenseUrl;

  @Value("${starter.openapi.contactEmail:FIXME@twdps.io}")
  private String contactEmail;

  @Value("${starter.openapi.contactUrl:https://example.twdps.io/}")
  private String contactUrl;

  @Value("${starter.openapi.contactName:Example}")
  private String contactName;

  @Value("${starter.openapi.serverUrl:http://localhost:8080}")
  private String serverUrl;

  @Autowired(required = false)
  private List<CustomSchemaProvider> schemaProviders;

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
            .servers(Arrays.asList(new Server().url(serverUrl)));

    if (null != schemaProviders && schemaProviders.size() > 0) {
      Components components = new Components();
      schemaProviders.stream().forEach(p -> components.addSchemas(p.getName(), p.create()));
      config.components(components);
    }

    return config;
  }
}
