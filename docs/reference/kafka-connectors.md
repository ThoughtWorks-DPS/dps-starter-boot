# Kafka Connectors

Provide support for simple producer / consumer sonnections to Kafka message queues.

## Overview

Kafka provides unique qualities for queuing and persistence capabilities.
This package provides simple consumer and producer classes while also enforcing some structure and best practices relative to event-driven systems architecture.

### What it is

Easy to use `KafkaProducer` and `KafkaConsumer` classes / interfaces.
Best-practice configuration for typical event-driven architecture.

### What it isn't

Highly optimized configurations for your particular use-case.

## Spring Boot Configuration

Configuration classes are split between producer and consumer use-cases.
There is also configuration class for initializing the Kafka ObjectMapper to serialize timestamps conforming to the RFC3339 / ISO8601 standard format.

Included are a set of `*ConfigProperties` classes which model the standard `spring.kafka.*` application property configuration.
This provides the required configuration property model to mimic `spring.kafka.*` properties from an alternate root (i.e. `my-application.kafka.*`)

### KafkaConsumerConfig

`KafkaConsumerConfig` initializes the listener container factory with concurrent listener implementation.

### KafkaProducerConfig

`KafkaProducerConfig` provides the thread pool `Executor` object for handling async message send.
It gets thread pool configuration properties from the `KafkaConnectorConfigProperties`, which is autowired.

You can choose to supply a custom config properties object, or supply your own `Executor` implementation.

Both `KafkaProducerConfig` and `KafkaConnectorConfigProperties` are enabled via the property `starter.boot.kafka-connector.producer.enabled=yes`

#### SpringKafkaProducerConfigProperties

This class maps the `KafkaProducerConfigProperties` class to the `spring.kafka` property prefix.

As an example, the `TestKafkaConfigProperties` class maps to `starter.boot.kafka-connector.producer` property prefix. 

```java
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "starter.boot.kafka-connector.producer")
public class TestKafkaProducerConfigProperties extends KafkaProducerConfigProperties { }
```

### KafkaSerdeObjectMapperConfig

The `KafkaSerdeObjectMapperConfig` initializes the Kafka ObjectMapper to serialize timestamps conforming to the RFC3339 / ISO8601 standard format.

## Implementation classes

### CustomJsonDeserializer

The `CustomJsonDeserializer` is derived from the core Spring Kafka `JsonDeserializer<>` class.
We create our own `ObjectMapper` class based on the Kafka `JacksonUtils.enhancedObjectMapper()` and customize it for RFC3339 / ISO8601 conformance.
It's a static function so that we can use it in the superclass constructor to inject it into the Kafka Consumer classes.

This is a much simpler method of customizing the `ObjectMapper` than supplying custom Kafka factory classes.

Configure the custom value deserializer class in `application.properties` file:

```properties
kafka.consumer.value-deserializer = io.twdps.starter.boot.kafka.CustomJsonDeserializer
```

### CustomJsonSerializer

The `CustomJsonSerializer` is derived from the core Spring Kafka `JsonSerializer<>` class.
We create our own `ObjectMapper` class based on the Kafka `JacksonUtils.enhancedObjectMapper()` and customize it for RFC3339 / ISO8601 conformance.
It's a static function so that we can use it in the superclass constructor to inject it into the Kafka Consumer classes.

This is a much simpler method of customizing the `ObjectMapper` than supplying custom Kafka factory classes.

Configure the custom value serializer class in `application.properties` file:

```properties
kafka.consumer.value-serializer = io.twdps.starter.boot.kafka.CustomJsonSerializer
```

### IdentifiableMessage

The interface `IdentifiableMessage` defines the `getMessageIdentifier()` function so that the Producer and Consumer classes can uniquely identify individual messages.

### TypedIdentifiableMessage

`TypedIdentifiableMessage` implements `IdentifiableMessage` and creates a message wrapper for a specific class type.
It also uses a static `AtomicInteger` to provide an atomically incrementing message identifier.

When using `IdentifiableMessage` classes, configure the custom key (de)serializer class in `application.properties` file:

```properties
kafka.consumer.key-serializer = org.apache.kafka.common.serialization.IntegerSerializer
kafka.producer.key-serializer = org.apache.kafka.common.serialization.IntegerSerializer
```

### KafkaConsumer

The `KafkaConsumer` class provides a pattern for building your own Kafka Consumer beans.
The mechanism that Spring uses to construct the consumer beans is not friendly to abstraction into base classes.

### KafkaProducer

The `KafkaProducer` class provides a super-class which handles most of the work.
It provides a few flavors of the `send()` function.
Functions that do not include the `topic` name will be provided by the topic name defined in the configuration properties `kafka.topic.name`.

```java
  public void send(T payload);
  public void send(String topic, T payload);

  public ListenableFuture<SendResult<Integer, T>> sendMessage(final T message);
```

Each of the functions eventually call the following to send the message.
The `@Async` annotation specifies the `Executor` thread pool defined in `KafkaProducerConfig`.

```java
  @Async("kafkaProducerExecutor")
  public ListenableFuture<SendResult<Integer, T>> sendMessage(String topic, final T message);
```

### LatchedKafkaConsumer

The `LatchedKafkaConsumer` class is primarily used for testing.
It contains a `CountdownLatch` which is decremented upon message receipt.
This allows the unit test code to wait on the latch and continue the tests as soon as the message arrives. 
