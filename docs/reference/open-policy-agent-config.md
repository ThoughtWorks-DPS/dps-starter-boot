# Open Policy Agent

Spring-Boot configuration for defining http access policy in conjunction with the Open Policy Agent service for authorizing API requests.

## Overview

Provide specific configuration settings for using an Open Policy Agent implementation to provide security and authorization for REST API endpoints.

### What it is

Automatic configuration of http configuration for using OPA to provide security and authentication.

### What it isn't

A security blanket.

## OpenPolicyAgentConfig

The `OpenPolicyAgentConfig` disables csrf protection.

The configuration is conditional on the property `starter.open-policy-agent-config.enabled`

The actual security filter is implemented by including the `com.bisnode.opa:opa-filter-spring-boot-starter` package in the application classpath dependencies list.

```groovy
    implementation 'com.bisnode.opa:opa-filter-spring-boot-starter'
```

We define the following properties needed by the `filter-spring-boot-starter` package:  

```yaml
opa:
  filter:
    enabled: true
    document-path: authz
    instance: http://opa:8181
    endpoints-whitelist: /actuator/**,/swagger-ui/**,/v3/api-docs/**
```

* `enabled` turns on OPA policy-based security filters
* `document-path` defines the document path in the OPA server which will provide the authorization.
* `instance` defines the URL for the OPA authorization server.
* `endpoints-whitelist` defines URL paths which will not be protected by the OPA authorization policy.
