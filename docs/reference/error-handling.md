# Error Handling

Provide framework for mapping Java exceptions to HTTP error codes.
This library also provides service-specific exception mappings.

## Overview

This package provides error handling infrastructure built on the framework provided by Zalando's RFC7807-compliant [problem-spring-web](https://github.com/zalando/problem-spring-web) library.

### What it is

Maps custom exceptions to HTTP error codes via standard Spring `@ControllerAdvice` classes.
It also augments the error payload with OpenTracing trace identifiers for easier problem troubleshooting.

### What it isn't

A comprehensive mapping of every exception.

## Spring Configuration

The configuration for the exception mapping occurs with two classes: `ErrorHandlerConfig` and `SecurityProblemConfig`.

`ErrorHandlerConfig` maps the Zalando ProblemModule into the ObjectMapper, to allow the error payloads to be serialized in the HTTP response.

`SecurityProblemConfig` hooks the Zalando `SecurityProblemSupport` into the http exception handling for authentication and access denied handling.

## ControllerAdvice classes

### TraceableAdviceTrait

`TraceableAdviceTrait` is a base interface with default implementation which extracts Open Tracing trace information from the headers for inclusion in the error payload.
This lets consumers more readily examine the distributed trace when troubleshooting.
It also enables external consumers to provide more detailed problem reports when troubleshooting issues.

The specific exception mapping `*AdviceTrait` interfaces are derived from `TraceableAdviceTrait`.

### SecurityHandlerAdvice

The `SecurityHandlerAdvice` creates a `@ControllerAdvice` class as a concrete implementation of the `SecurityAdviceTrait` interface, in effect activating the handlers for security-related exceptions.

### DownstreamTimeoutAdviceTraits

Maps `DownstreamTimeoutException` to the `REQUEST_TIMEOUT` HTTP error code.

### RequestValidationAdviceTraits

Maps `RequestValidationException` to the `BAD_REQUEST` HTTP error code.

### ResourceNotFoundAdviceTraits

Maps `ResourceNotFoundException` to the `NOT_FOUND` HTTP error code.

