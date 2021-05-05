# Exceptions

Service-specific exception definitions

## Overview

We define exceptions for common errors which occur within services.

### What it is

A few common exceptions.

### What it isn't

Every possible thing that could go wrong.

## System Exceptions

This is a small collection of exceptions which define common errors which occur within services.

### DownstreamTimeoutException

`DownstreamTimeoutException` indicates that a downstream service request timed out.

### RequestValidationException

`RequestValidationException` indicates that some semantic or content errors exist in the request payload.

### ResourceNotFoundException

`ResourceNotFoundException` indicates that some required resource was not available.
