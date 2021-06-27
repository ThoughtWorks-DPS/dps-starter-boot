[![Maintainability](https://api.codeclimate.com/v1/badges/FIXME_TOKEN/maintainability)](https://codeclimate.com/repos/FIXME_TOKEN/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/FIXME_TOKEN/test_coverage)](https://codeclimate.com/repos/FIXME_TOKEN/test_coverage)
[![CircleCI](https://circleci.com/gh/ThoughtWorks-DPS/dps-starter-boot.svg?style=shield&circle-token=FIXME_TOKEN)](https://app.circleci.com/pipelines/github/ThoughtWorks-DPS/dps-starter-boot?branch=main)

# starter-boot

starter-boot is a project to provide many of the same developer affordances that Spring Boot provides.

## Developer environment

### Build requirements

- Java 11

Mac users can execute the following command to install the latest version of Java:

```bash
brew install java
```

Verify that the correct Java version is being used by running:

```bash
java -version
```

If needed, set your machine to use the correct version of Java (JAVA_VERSION in the command below) that was installed by
adding the following line to your .bashrc or .zshrc:

```bash
export JAVA_HOME=/usr/local/Cellar/openjdk/<JAVA_VERSION>/libexec/openjdk.jdk/Contents/Home 
```

Reload the shell by running:

```bash
source .bashrc
#or
source .zshrc
```

### Dev Tools Setup

- `cd scripts`
- Run `sh mac-dev-tools.sh`

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

The proxy should log the latencies and error rates to the Scientist Dashboard (i.e. splunk) that can aggregate the results and let you know when it's safe to fully migrate / retire the old version.
It should be able to manage sampling methods as well as throttling down the new version in case it can't keep up with the load.

There already exist a couple Java libraries that do similar things.
However, the functionality they offer exists at a lower level than what I'm thinking of.

- [https://github.com/mhvelplund/scientist.java](https://github.com/mhvelplund/scientist.java)
- [https://github.com/rawls238/Scientist4J](https://github.com/rawls238/Scientist4J).

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
