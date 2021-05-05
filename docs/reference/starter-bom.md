# Starter BOM

The Starter Bill-of-Materials package utilizes common Spring-Boot patterns to provide a dependency package for starter-kit projects.
This `starter-bom` package specifies the set of library dependencies and the version constraints.
These version constraints can be useful to drive propagation of CVE updates, for example, by updating minimum required versions to resolve a CVE.

## Overview

Describe the API services and business capabilities.
An overview of the domain boundaries is helpful to align consumer expectations.
This is a good opportunity to correct any common assumptions or pre-conceptions.

### What it is

A list of commonly used packages and their versions that:

* work well together (i.e. similar to Spring-Boot curated starter BOM packages)
* are not known to cause any existing CVE issues

This is similar to the Apache Maven concept of `dependencyManagement` entries.

### What it isn't

This `starter-bom` package is not a restricted list of packages.
In other words, if you choose to use versions more recent than those listed, it is your choice (and therefore also responsibility) to manage your own package versions.

## Usage

The typical usage is to include the `starter-bom` package as a platform dependency.
As a platform dependency, it provides the required versions for the libraries listed as (potential) dependencies.

Referencing the `starter-bom` allows us to just list the `<groupId>:<artifactId>` and omit the version (which is supplied by the platform BOM dependency).

### Example Usage

From the `open-api` sub-module `build.gradle` file:

```groovy
plugins {
  id 'starter.std.java.library-spring-conventions'
}

dependencies {
  annotationBom platform(project(':starter-bom'))
  checkstyleRules platform(project(':checkstyle-bom'))

  implementation "org.springdoc:springdoc-openapi-ui"
  implementation "org.springdoc:springdoc-openapi-webmvc-core"
  implementation "org.springdoc:springdoc-openapi-security"
  implementation "org.springdoc:springdoc-openapi-data-rest"
  testImplementation project(':test-example')
}
```
