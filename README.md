# starter-boot

starter-boot is a project to provide many of the same developer affordances that Spring Boot provides.


## starter-bom

Currently we have implemented the `starter-bom` which provides a Bill of Materials for defining common library as well as  Spring Boot version requirements.
This provides a central artifact to provide governance around minimum required package versions (think CVE mitigation) in a way that is easy to implement across the codebase.

## tracing

We have also created Spring configuration classes for implementing OpenTracing using Zipkin configuration.
This configuration also provides span/trace logging across calls to JPA persistence, which is very helpful to unify monitoring and metrics.

## checkstyle

Provides the checkstyle settings as an artifact for team consumption as a default starting point.
Again, having central defaults aids governance when dealing with newly discovered CVEs or other issues that need to be surfaced.

## checkstyle-bom

Defines the version of checkstyle configurations, enables easy updates Provides the checkstyle settings as an artifact for team consumption as a default starting point.
Again, having central defaults aids governance when dealing with newly discovered CVEs or other issues that need to be surfaced.

## plugins

Externalizes the Gradle "mixins" that might live in the buildSrc as an artifact that can be used as easily overridable default behavior.
This allows the starter kit to rely on the toplevel starter.std.* plugins, while giving teams domain-oriented components that can be overridden without requiring tons of duplication.
See the [README.md](./buildSrc/README.md) in the buildSrc directory for more details.

## scientist

This is a theoretical package based on the ideas implemented in Github Scientist.

Imagine I started on postgres, but due to volume and use-case, I need to migrate to C* and offer API v2.
I have the SPI, so ideally I'd like to start with the data migration to a new schema before I start offering v2 of my API.
Conceptually, that means creating a new implementation of my SPI that works with the new schema, and it should behave the same as my original.

Imagine:
```java
@Scientist(original = "CurrentImplOfSPI.class", target = "NewImplOfSPI.class")
interface ScientistProxyOfSPI implements SPI 
{}
```

Annotating the interface lets you inject a Scientist Proxy into your controller instead of the old implementation.

The Scientist proxy should be able to be auto-constructed based on the API, and given the two classes, perform all the mechanics of delegating to each implementation.
The Proxy should return the results of the original, as well as compare those results with the new.

The proxy should log the latencies and error rates to the Scientist Dashboard (i.e. splunk or some such) that can aggregate the results and let you know when it's safe to fully migrate and retire the old version.
It should be able to manage sampling methods as well as throttling down the new version in case it can't keep up with the load.

There already exist a couple Java libraries that do similar things [ https://github.com/mhvelplund/scientist.java, https://github.com/rawls238/Scientist4J ].
However, the functionality they offer exists at a lower level than what I'm thinking of.


## Release Process

We use `axion-release-plugin` Gradle plugin to provide git-based versioning.
The version for the package is computed from the nearest version tag in the git repository.
This is helpful for CI pipelines, and simplifies the classic maven-release-plugin process.

A sample flow (from the [axion docs](https://axion-release-plugin.readthedocs.io/en/latest/)):

```bash
% git tag
project-0.1.0

% ./gradlew currentVersion
0.1.0

% git commit -m "Some commit."

% ./gradlew currentVersion
0.1.1-SNAPSHOT

% ./gradlew release

% git tag
project-0.1.0 project-0.1.1

% ./gradlew currentVersion
0.1.1

% ./gradlew publish
published project-0.1.1 release version

% ./gradlew markNextVersion -Prelease.version=1.0.0

% ./gradlew currentVersion
1.0.0-SNAPSHOT
```

