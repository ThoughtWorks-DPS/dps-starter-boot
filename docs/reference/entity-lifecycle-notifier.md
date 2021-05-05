# Entity Lifecycle Notifier

Entity model mutation notifications (i.e. create, update, delete).

## Overview

Borrowing principles from Data Mesh, we want to emit a stream of mutation events as an event stream.
The event stream supports general analytics use-cases.
It also enables additional business value for external consumers which need to react to changes in the underlying data.

This package provides and interface for implementing notification over a specific communication channel.
It also provides the core data model which provides consistent metadata to the business entities which mutate.

### What it is

Not much.
This package only supplies the interface and the metadata model.
Implementation is left as an exercise to the reader.

### What it isn't

This framework does not identify exactly what changed.
It only communicates the new state of the entity.

## Implementation

The core classes include the `EntityLifecycleNotification` which defines the payload being emitted, and the `EntityLifecycleNotifier` is an interface which defines the notification functions.

### EntityLifecycleNotification

The `EntityLifecycleNotification` (ELN) is the event object that gets emitted to consumers.
It contains metadata defining:

* schema version of the entity 
* mutation operation
* when the operation took place
* URI of the agent performing the operation
* the resulting state of the entity

### TimestampProvider

The `TimestampProvider` is a simple interface to provide the timestamp associated with an ELN when not provided.

There are two implementations provided, one for supplying the current timestamp, and another for supplying a static timestamp when testing.

#### CurrentTimestampProvider

This eponymous provider returns the current timestamp at the time it is called.

#### MemoizedTimestampProvider

The `MemoizedTimestampProvider` is initialized with a timestamp in the constructor.
This constant timestamp is returned as-is whenever the `TimestampProvider.now()` function is called.
This is particularly useful for testing, since it provides a way to deterministically pin the timestamps to a known value.

### EntityLifecycleNotifierConfig

The `EntityLifecycleNotifierConfig` class provides default implementations for the `EntityLifecycleNotifier` and `TimestampProvider` bean classes.
Each of these are annotated `@ConditionalOnMissingBean`.
To override either of these, just provide your own Spring-Boot configuration class supplying a specific implementation. 

### EntityLifecycleObjectMapperConfig

The `EntityLifecycleObjectMapperConfig` class sets a few options in the ObjectMapper to serialize timestamp classes in the RFC3339 / ISO8601 standard format.

### NoopEntityLifecycleNotifier

The `NoopEntityLifecycleNotifier` does nothing.
It's only purpose in life is to supply a do-no-harm default.

## Reference

See the Javadoc for specific details on the implementation classes.
