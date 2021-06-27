# Gradle Mixins

The mixins are meant to provide snippets of Gradle configuration based on specific functional groupings.
The groups are identified as starter scripts, and roughly grouped:

* java - basic configuration around a typical build objective
* metrics - build timing configuration
* std - top-level configs based on target type

The `std` level of configuration is organized by artifact type.
This is the only level meant to organize or include other configs.
The design is meant to be easily overridden by teams that wish to depart from the standard configs.
Teams are able to just copy the `std` level conventions to their project and override those they wish to change.
Items they don't need to change can continue to be referred in their local configs.

The intention is that the `starter-boot` package can externalize these mixins as plugins so the team can continue to refer to those mixins that do not need to change.
To that end, do not include or build upon other convention files, except at the `std` level.

The only (current) exception to this rule is the open-tracing configuration, which pulls in a few standard plugins.

## starter.java.build-conventions.gradle

Provides a set of common dependencies for typical build and test.
Includes proper dependencies for lombok and mapstruct annotation processing.
Also includes typical dependencies for unit testing with junit jupiter.

## starter.java.build-copyright-conventions.gradle

Provides a task `updateCopyrights` which will scan code for a copyright string and update the current year.
This is only done for files which are modified (i.e. in a git changeset)

## starter.java.build-git-conventions.gradle

Provides configuration for the `org.ajoberstar.grgit` plugin.
It will dynamically apply the plugin based on the presence of a `.git` folder in the root project directory.

## starter.java.checkstyle-conventions.gradle

Setting for running checkstyle, pulls configuration from the checkstyle jar.

## starter.java.config-conventions.gradle

Provides configurations for platform (BOM) dependencies, to ensure non-api/runtime configurations are set properly.

## starter.java.container-conventions.gradle

Provides docker container settings

## starter.java.container-spring-conventions.gradle

Provides customization of `docker` and `dockerRun` for spring-boot specific container construction.

## starter.java.coordinate-conventions.gradle

Provides shortcuts for overriding group and version.
NOTE: Most likely obsolete, in favor of specifying group directly in the gradle.properties file, and using axion to supply version based on git tags.

## starter.java.deps-build-conventions.gradle

General library dependencies for common non-spring-boot libs.

## starter.java.deps-integration-conventions.gradle

General library dependencies for common integration tests.

## starter.java.deps-plugin-conventions.gradle

Library dependencies for developing gradle plugins

## starter.java.deps-plugin-integration-conventions.gradle

Library dependencies for integration testing gradle plugins

## starter.java.deps-tests-conventions.gradle

General library dependencies for unit testing.

## starter.java.doc-asciidoc-conventions.gradle

Asciidoc configurations for generating documentation from Asciidoc (via Asciidoctor)

## starter.java.doc-springdoc-conventions.gradle

SpringDoc configurations for generating OpenAPI specs

## starter.java.doc-swagger-conventions.gradle

Swaggerhub configurations

## starter.java.open-tracing-common-conventions.gradle

Opentracing configuration, currently just a placeholder for including a few plugins.

## starter.java.property-conventions.gradle

Functions for providing default values if properties or variables aren't defined.

## starter.java.publish-bootjar-conventions.gradle

Configurations for publishing spring-boot fat jar files

## starter.java.publish-jar-conventions.gradle

Configurations for publishing jar files

## starter.java.publish-pom-conventions.gradle

Configurations for publishing BOM packages (java-platform).

## starter.java.publish-repo-conventions.gradle

Configurations for where to publish packages (currently configurations for github packages)

## starter.java.release-conventions.gradle

Configurations for axion-release-plugin.

## starter.java.repo-altsource-conventions.gradle

Configurations for specifying a configurable repository (`mavenRepository`, `MAVEN_REPO_USERNAME`, `MAVEN_REPO_PASSWORD`)

## starter.java.repo-default-conventions.gradle

Configurations for specifying standard defaults (local, mavenCentral, JCenter)

## starter.java.repo-local-conventions.gradle

Configurations for specifying only local maven `~/.m2` repository

## starter.java.repo-starter-conventions.gradle

Configurations for specifying starter-bom Github Packages repository

## starter.java.spotless-conventions.gradle

Configuration for spotless code linting

## starter.java.style-conventions.gradle

Include for checkstyle

## starter.java.test-conventions.gradle

Configuration for test task, reporting

## starter.java.test-gatling-conventions.gradle

Configuration for gatling performance testing

## starter.java.test-integration-conventions.gradle

Configuration for integration testing

## starter.java.test-jacoco-aggregation-conventions.gradle

Configuration for aggregating jacoco test reports into one unified report.

## starter.java.test-jacoco-conventions.gradle

Configuration for jacoco

## starter.java.test-unit-conventions.gradle

Configuration for unit tests (currently empty placeholder)

## starter.java.versions-conventions.gradle

Configuration for finding dependencies with outdated versions

## starter.metrics.build-time-tracker-conventions.gradle

Configuration for tracking how long a build takes, using `net.rdrei.android.buildtimetracker`

## starter.metrics.talaiot-conventions.gradle

Configuration for tracking how long a build takes, using talaiot.
NOTE: No configuration set for shipping metrics to an aggregator, but the capability exists.

## starter.std.java.application-conventions.gradle

Top-level configuration of all the typical standard configurations for a Spring Boot application package.

## starter.std.java.bom-conventions.gradle

Top-level configuration of all the typical standard configurations for a Bill of Materials package

## starter.std.java.cli-conventions.gradle

Top-level configuration of all the typical standard configurations for a java cli (untested)

## starter.std.java.library-conventions.gradle

Top-level configuration of all the typical standard configurations for a normal Java jar.

## starter.std.java.plugin-conventions.gradle

Top-level configuration of all the typical standard configurations for developing Gradle plugins.

## starter.std.java.shell-conventions.gradle

Top-level configuration for shell scripts.
