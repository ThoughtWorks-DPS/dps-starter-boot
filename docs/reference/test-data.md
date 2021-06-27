# Test Data

Framework for loading test data sets from various sources.
Loading data from property file contents is supported with minimal effort in the client code.
These classes help consolidate test data sets into externalized configuration for easier management.

## Overview

The `test-data` library provides classes for retrieving sample data.

Centralizing the test data for unit tests into a specific class makes it easier during the skeleton rename process.
Only one class needs to be refactored, instead of individual test class member data instances scattered across the codebase.

In the future, it is intended to provide classes which allow annotation of the test data structure.
The annotations would provide a mechanism to use Faker library to generate realistic test data.
This is most useful when generating large amounts of randomized test data.

### What it is

Framework for flexibly defining test data samples.

### What it isn't

Production data.

## Usage

Activating the Test Data is done by a combination of classpath dependencies and small derived classes.
Spring auto-configuration takes care of the configuration of the test data.
Look at the unit test code for hints as to how to use this framework in service code.

### Example Code

First, define the data that will be managed:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AccountData {

  private String id;
  private String userName;
  private String pii;
  private String firstName;
  private String lastName;
}
```

Define the Data Factory class.
We can extend `GenericDataFactory<T>` with the class type.
We mark it as a `@Component` so that it can be autowired into configuration and test classes.
This also allows us to rely on the `DataLoader<T>` class instance to be autowired via the ctor.

```java
@Component
public class AccountDataFactory extends GenericDataFactory<AccountData> {

  AccountDataFactory(DataLoader<AccountData> loader) {
    super(loader);
  }
}
```

You must provide a `DataLoader<T>` implementation for the autowiring to work.
We define subclass of `GenericDataLoader<T>` as a Spring properties class, so that we can autoload data from the application.yml.
The property bean class relies on the `@ConfigurationProperties("test-data.account")` annotation to define the root property name.

```java
@Component
@ConfigurationProperties("test-data.account")
public class AccountDataProperties extends GenericDataLoader<AccountData> {}
```

### Example Test Data

The test data is specified in the normal application.yml file under the test code tree `resources` folder.

```yaml
test-data:
  account:
    data:
      default:
        user-name: asmith
        first-name: Agent
        last-name: Smith
        pii: eigenvalue
        id: uuid-unit-vector
      matrix:
        user-name: neo
        first-name: Neo
        last-name: None
        pii: sunglasses
        id: Reeves
      raiders:
        user-name: junior
        first-name: Indiana
        last-name: Jones
        pii: archeologist
        id: Ford
      bogus:
        user-name: theodore
        first-name: Ted
        last-name: Logan
        pii: wyldstallyns
        id: Reeves
    collections:
      default:
        - user-name: asmith
          first-name: Agent
          last-name: Smith
          pii: eigenvalue
          id: uuid-unit-vector
        - user-name: neo
          first-name: Neo
          last-name: None
          pii: sunglasses
          id: Reeves
      starwars:
        - user-name: hsolo
          first-name: Han
          last-name: Solo
          pii: Falcon
          id: Ford
        - user-name: lskywalker
          first-name: Luke
          last-name: Skywalker
          pii: saber
          id: Hamill
        - user-name: dvader
          first-name: Anakin
          last-name: Skywalker
          pii: darkhelmet
          id: Jones
```

## Future Development

In the future, there may be a `FakerDataLoader<T>` which uses annotations on the data type in conjunction with the Faker library to generate realistic yet random data samples.
