# SpringDoc

Spring-Boot configuration for SpringDoc library.
Configures code-based annotations processing to help generate OpenAPI v3.0 documentation.

## Overview

The configuration for SpringDoc specifies three groups for organizing API endpoint definitions.

* `/v1/**`: normal APIs
* `/admin/v1/**`: Administrative APIs
* `/internal/v1/**`: Internal-only APIs

### Example Usage

```groovy
plugins {
  id 'starter.std.java.application-conventions'
}

dependencies {
  annotationBom platform("io.twdps.starter:starter-bom:${starter_boot_version}")
  // ...
  implementation 'io.twdps.starter:springdoc'
  // ...
}
```
