package io.twdps.starter.boot.kafkaconnectors.spi;

public interface IdentifiableMessage {
  Integer getMessageIdentifier();
}
