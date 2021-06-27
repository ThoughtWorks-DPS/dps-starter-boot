# Kafka Entity Lifecycle Notifier

Kafka implementation of a notifier which uses Kafka to communicate Entity mutations.

## Overview

Kafka provides unique qualities for queuing and persistence capabilities.
This package provides a producer class for publishing `LifecycleEntityNotification` messages on a Kafka queue.

### What it is

An implementation of the Entity Lifecycle Notifier using Kafka as the communication mechanism.

### What it isn't

My pet rock.

## Spring Boot Configuration

Included are a set of `KafkaEntityLifecycleNotifierConfigProperties` classes which model the standard `starter.boot.kafka-lifecycle-notifier.producer` application property configuration.
This provides the required configuration property model to mimic `spring.kafka.*` properties from an alternate root (i.e. `my-application.kafka.*`)

### KafkaEntityLifecycleNotifierConfig

The standard `spring.kafka.*` configuration properties are defined at the `starter.boot.kafka-lifecycle-notifier.producer` prefix.

`KafkaEntityLifecycleNotifierConfig` supplies the Kafka implementation of the `EntityLifecycleNotifier` for use in service implementations.

It also defines a `NewTopic` bean to create the defined topic from `starter.boot.kafka-lifecycle-notifier.producer.topic.name`

### TimestampProviderConfig

`TimestampProviderConfig` provides the `CurrentTimestampProvider` if no other timestamp provider is defined.

## Implementation classes

### KafkaEntityLifecycleNotifier

The `KafkaEntityLifecycleNotifier` derives from the `KafkaProducer` and `EntityLifecycleNotifier`
It implements the `EntityLifecycleNotifier.notify()` function, creates the `KafkaLifecycleEntityNotification` object, and uses `KafkaProducer.send()` to publish the notification.

Configure the custom value serializer class in `application.properties` file:

```properties
starter.boot.kafka-lifecycle-notifier.producer.value-serializer = io.twdps.starter.boot.kafka.CustomJsonSerializer
```
