# checkstyle

This package provides standard Checkstyle code linting rules.
The rules are packaged in a jar file, and referenced by its own BOM package to make it easy to load the latest ruleset.

## Overview

Checkstyle rules are a set of linting and code formatting rules.
We use the Google Checkstyle rules as a good starting point.
The `starter.java.lint-checkstyle-conventions` plugin automatically incorporates the linting as a `check` task dependency.

### What it is

A set of standardized linting rules.

### What it isn't

Guaranteed code quality.
You still have to apply some intelligence to the problem.
However, linting helps to catch bugs earlier in the development process.

## Example Usage

From the `open-api` sub-module `build.gradle` file:

```groovy
plugins {
  id 'starter.std.java.library-spring-conventions'
}

dependencies {
  annotationBom platform(project(':starter-bom'))
  checkstyleRules platform(project(':checkstyle-bom'))
  // ...
}
```

To run the `checkstyle` task directly:

```bash
gradlew :module:checkstyle
```

## Tips

There are times when having a long line is unavoidable (for example an import statement).
When this occurs, it is possible to turn off/on the Checkstyle checking via comments of the form:

```java
// CSOFF: LineLength
import io.twdps.some.really.stupendously.verbose.pkg.name.blah.blah.you.get.the.idea.TrivialImplementation;
// CSON: LineLength
```

The `CSOFF` / `CSON` directive works for any Checkstyle violation.
Follow the example for `LineLength` but supply the desired error name.
