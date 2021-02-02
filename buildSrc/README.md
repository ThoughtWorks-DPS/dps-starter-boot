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

I think the only exception to this rule is `starter.java.style-conventions`, which includes `starter.java.checktyle-conventions`.
This was only allowed because `style-conventions` is aggregating `checkstyle` and `spotless`.
If 'spotless' gets more complicated, then these two should be split and propagated upwards instead of being aggregated under `style-conventions`.



## starter.java.build-conventions.gradle

Provides a set of common dependencies for typical build and test.
Includes proper dependencies for lombok and mapstruct annotation processing.
Also includes typical dependencies for unit testing with junit jupiter.

## starter.java.checkstyle-conventions.gradle

Setting for running checkstyle, pulls configuration from the checkstyle jar.

## starter.java.config-conventions.gradle

Provides configurations for platform (BOM) dependencies, to ensure non-api/runtime configurations are set properly.

## starter.java.container-conventions.gradle

Provides docker container settings

## starter.java.coordinate-conventions.gradle

Provides shortcuts for overriding group and version.
NOTE: Most likely obsolete, in favor of specifying group directly in the gradle.properties file, and using axion to supply version based on git tags.

## starter.java.gatling-conventions.gradle

Gatling configuration for running stress tests

## starter.java.open-tracing-common-conventions.gradle

Typical dependencies to implement open tracing.

## starter.java.publish-jar-conventions.gradle

Configurations for publishing jar files

## starter.java.publish-pom-conventions.gradle

Configurations for publishing BOM packages (java-platform).

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

## starter.java.style-conventions.gradle

Configuration for checkstyle and spotless

## starter.java.swagger-conventions.gradle

Swaggerhub configurations

## starter.java.test-conventions.gradle

Configuration for jacoco

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

