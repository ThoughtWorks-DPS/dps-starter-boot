package io.twdps.starter.boot.tracing;

import brave.Tracing;

@FunctionalInterface
public interface ZipkinTracerCustomizer {

  void customize(Tracing.Builder builder);
}
