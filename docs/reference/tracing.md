# Tracing

Spring-Boot configuration for Open Tracing configuration.
Also provides configuration for JPA persistence calls, allowing database queries to be traced within the same distributed request span.

## Overview

The `tracing` library provides configuration to set up Open Tracing logging for API requests.
It also sets up the required configuration to trace JPA persistence queries in the same way it traces distributed API calls to other services.

The configuration emits metadata regarding the persistence queries, including the SQL query used in the call.

### What it is

Configuration for Open Tracing (soon to be Open Telemetry)

### What it isn't

Logging

## Usage

Activating the Open Tracing is done by simply including the package as one of your application dependencies.
Spring auto-configuration takes care of the configuration of the tracing capabilities.

The external Open Tracing dependencies are defined in `starter.java.deps-open-tracing-common-conventions`.
This is a normal part of the `starter.std.java.library-conventions` plugin.
Since the dependencies are specified as `api` dependencies, they will be included in your application as transitive dependencies.

### Configuration

The auto-configuration bean relies on `spring.application.name` defined in `application.properties` settings for the service name to attach to the tracing info.

### Example Usage

```groovy
plugins {
  id 'starter.std.java.application-conventions'
  id 'starter.java.config-conventions'
}

dependencies {
  annotationBom platform("io.twdps.starter:starter-bom:${starter_boot_version}")
  // ...
  implementation 'io.twdps.starter:tracing'
  // ...
}
```
