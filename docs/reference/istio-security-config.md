# Istio Security Configuration

Configure Spring-Boot application with Istio-specific settings.

## Overview

Provide specific configuration settings when running within the Istio service mesh.

> NOTE: This is (temporarily) deprecated.
> Current configuration for OPA is found in the `open-policy-agent-config` package.
> Other than OPA, there are no other Istio-specific configurations known to be necessary (at this time).

### What it is

Automatic configuration of Istio-related settings for use within the Istio service mesh.

### What it isn't

Don't expect it to make your lunch.

## IstioDisableSecurityConfig

Disables security checks on all paths, and also disables csrf protection.

