# Gradle Build Plugins

A collection of Gradle Build Plugins defining standard build processes for common build tasks.

## Overview

The Gradle Plugins are a set of Groovy-based plugins of the sort normally found in the ./buildSrc folder.
The plugins are build at a fairly-low level of granularity.

This is an intentional choice to provide opportunity for development teams to easily customize the build.
The tight focus allows teams to override small subsets of the standard build functionality, without taking ownership of more than necessary.

Teams are able to continue to rely on the standard parts of the build, while enjoying the freedom to customize relative to their unique needs.

## Guidelines

### Naming Conventions

The plugins follow a clear naming convention to help identify the purpose of each plugin.

#### Module-specific plugins

Plugins starting with `starter.std.*` are considered top-level plugins.
Each of these plugins is geared toward a specific type of sub-module.
Current types include:

* `starter.std.java.application-conventions`: Spring-Boot application
* `starter.std.java.bom-conventions`: Bill-of-Materials package
* `starter.std.java.cli-conventions`: Command-line utility application
* `starter.std.java.library-conventions`: normal Java library
* `starter.std.java.library-spring-conventions`: Spring-Boot configuration package Java library 
* `starter.std.java.plugin-conventions`: Gradle Plugin
* `starter.std.java.shell-conventions`: (bash|sh) shell script utilities (primarily to drive script linting)

In each sub-module, it should be possible to create a sub-module build.gradle file with:

* `plugins {}` specifying the appropriate top-level plugin from the above list (`starter.std.java.*`)
* `dependencies {}` listing the specific dependencies for your module

#### Function-specific plugins

Plugins starting with `starter.java.*` are function-specific plugins, targeted at typical java build tasks.
The domain categories include:

* `starter.java.build-utils-*`: general build utility function definitions and configurations
* `starter.java.config-*`: configuration conventions
* `starter.java.config-*`: definitions for configuration archetypes
* `starter.java.container-*`: docker container support, docker-compose support
* `starter.java.deps-*`: standard dependencies for various build tasks
* `starter.java.doc-*`: documentation support (markdown, asciidoc, swagger, OpenAPI)
* `starter.java.lint-*`: static code analysis (checkstyle, spotless, shellcheck)
* `starter.java.publish-*`: configurations for publishing different types of artifacts
* `starter.java.release-*`: release configuration
* `starter.java.repo-*`: repository configurations for retrieving artifact dependencies
* `starter.java.test-*`: configuration for test tasks (unit, integration) and aggregated metrics
* `starter.java.version-*`: support for determining versioning

### Plugin Inclusion

We have adopted the policy to only include local plugins in the top-level `starter.std.java.*` plugins.
For any `starter.std.java.*` plugins, the policy is to only include external plugin dependencies.
The top-level plugins should define the dependent plugins in the specific order required to satisfy any inter-plugin dependencies.

These rules will allow teams to replace function-specific plugins on an as-needed basis with a minimum of effort.
The modifications required will supply:

* the team-specific `local.java.*` plugin definition
* the team-specific `starter.local.java.*` plugin definition with the individual `local.java.*` plugin referenced

This avoids teams being forced to override unrelated plugins locally to update cross-plugin dependency references.

#### Example

Suppose my team needs to modify how the configuration sets are defined.
I would start by defining the `local.java.config-conventions` file in my `./buildSrc/src/main/groovy` directory.

Once I've modified that file with the necessary additions, I need to hook it into my build.
First I define the `local.std.java.library-spring-conventions` file in my `./buildSrc/src/main/groovy` directory to include my local plugin.

```groovy
plugins {
    id 'starter.std.java.library-conventions' // (1)
    id 'local.java.config-conventions' // (2)
    id 'starter.java.build-utils-conventions' // (1)
}
```

* (1) continue to reference the standard plugins
* (2) this is our local version of the `local.java.config-conventions` plugin


