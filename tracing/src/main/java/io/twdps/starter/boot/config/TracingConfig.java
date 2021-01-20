package io.twdps.starter.boot.config;

import brave.Tracing;
import brave.handler.SpanHandler;
import brave.opentracing.BraveTracer;
import brave.sampler.BoundarySampler;
import brave.sampler.CountingSampler;
import brave.sampler.Sampler;
import io.opentracing.Tracer;
import io.twdps.starter.boot.config.properties.ZipkinConfigurationProperties;
import io.twdps.starter.boot.tracing.ZipkinTracerCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

import static zipkin2.codec.SpanBytesDecoder.JSON_V2;
import static zipkin2.codec.SpanBytesDecoder.PROTO3;
import static zipkin2.codec.SpanBytesEncoder.JSON_V1;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "opentracing.zipkin", name = "enabled", havingValue = "true")
public class TracingConfig {

  private String serviceName;

  public TracingConfig(  @Value("${spring.application.name:unknown-spring-boot}") String serviceName) {
    this.serviceName = serviceName;
  }
  /** create tracer config.
   *
   * @param spanHandler span handler implementation
   * @param sampler sampler implementation
   * @return tracer
   */
  @Bean
  public Tracer tracer(SpanHandler spanHandler, Sampler sampler) {

    Tracing braveTracing =
        Tracing.newBuilder()
            .localServiceName(serviceName)
            .supportsJoin(false)
            .addSpanHandler(spanHandler)
            .build();
    // use this to create an OpenTracing Tracer
    return BraveTracer.create(braveTracing);
  }

  /** create span handler config.
   *
   * @param properties properties for configuring span handler
   * @return span handler
   */
  @Bean
  public SpanHandler spanHandler(ZipkinConfigurationProperties properties) {
    String url = properties.getHttpSender().getBaseUrl();
    log.info("Zipkin URL:{}", url);
    if (properties.getHttpSender().getEncoder().name().equals(JSON_V2.name())
        || properties.getHttpSender().getEncoder().name().equals(PROTO3.name())) {
      url += (url.endsWith("/") ? "" : "/") + "api/v2/spans";
    } else if (properties.getHttpSender().getEncoder().name().equals(JSON_V1.name())) {
      url += (url.endsWith("/") ? "" : "/") + "api/v1/spans";
    }
    return AsyncZipkinSpanHandler.create(OkHttpSender.create(url));
  }

  /** configure sampler.
   *
   * @param properties properties for configuring sampler
   * @return sampler
   */
  @Bean
  public Sampler sampler(ZipkinConfigurationProperties properties) {
    if (properties.getBoundarySampler().getRate() != null) {
      return BoundarySampler.create(properties.getBoundarySampler().getRate());
    }

    if (properties.getCountingSampler().getRate() != null) {
      return CountingSampler.create(properties.getCountingSampler().getRate());
    }

    return Sampler.ALWAYS_SAMPLE;
  }

  /** customize tracer.
   *
   * @return customizer for zipkin tracer
   */
  @Bean
  public ZipkinTracerCustomizer zipkinTracerCustomizer() {
    return (Tracing.Builder builder) -> builder.supportsJoin(false);
  }
}
