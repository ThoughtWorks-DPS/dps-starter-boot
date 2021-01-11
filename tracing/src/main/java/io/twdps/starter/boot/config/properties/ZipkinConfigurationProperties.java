package io.twdps.starter.boot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import zipkin2.codec.SpanBytesEncoder;

@Component
@ConfigurationProperties("opentracing.zipkin")
public class ZipkinConfigurationProperties {

  private final HttpSender httpSender = new HttpSender();
  private final BoundarySampler boundarySampler = new BoundarySampler();
  private final CountingSampler countingSampler = new CountingSampler();

  private boolean enabled = true;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public HttpSender getHttpSender() {
    return httpSender;
  }

  public BoundarySampler getBoundarySampler() {
    return boundarySampler;
  }

  public CountingSampler getCountingSampler() {
    return countingSampler;
  }

  public static class HttpSender {

    /**
     * Zipkin base URL without endpoint e.g. /api/v2/spans
     */
    private String baseUrl = "http://localhost:9411/";
    /**
     * Encoding of spans sent to Zipkin server. Use {@link SpanBytesEncoder#JSON_V1} if you are
     * using older server.
     */
    private SpanBytesEncoder encoder = SpanBytesEncoder.JSON_V2;

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setBaseUrl(String url) {
      this.baseUrl = url;
    }

    public SpanBytesEncoder getEncoder() {
      return encoder;
    }

    public void setEncoder(SpanBytesEncoder encoder) {
      this.encoder = encoder;
    }
  }

  public static class BoundarySampler {

    private Float rate;

    public Float getRate() {
      return rate;
    }

    public void setRate(Float rate) {
      this.rate = rate;
    }
  }

  public static class CountingSampler {

    private Float rate;

    public Float getRate() {
      return rate;
    }

    public void setRate(Float rate) {
      this.rate = rate;
    }
  }
}
