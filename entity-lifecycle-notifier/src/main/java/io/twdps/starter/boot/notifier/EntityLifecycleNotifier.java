package io.twdps.starter.boot.notifier;

import io.twdps.starter.boot.notifier.EntityLifecycleNotification.Operation;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.ZonedDateTime;

@Service
public interface EntityLifecycleNotifier {

  TimestampProvider getTimestampProvider();

  default void created(Object obj, String version, URI actor) {
    created(obj, obj.getClass(), version, actor, getTimestampProvider().now());
  }

  default void created(Object obj, Class<? extends Object> type, String version, URI actor,
      ZonedDateTime ts) {
    notify(obj, type, version, Operation.CREATED, actor, ts);
  }

  default void updated(Object obj, String version, URI actor) {
    updated(obj, obj.getClass(), version, actor, getTimestampProvider().now());
  }

  default void updated(Object obj, Class<? extends Object> type, String version, URI actor,
      ZonedDateTime ts) {
    notify(obj, type, version, Operation.UPDATED, actor, ts);
  }

  default void deleted(Object obj, String version, URI actor) {
    deleted(obj, obj.getClass(), version, actor, getTimestampProvider().now());
  }

  default void deleted(Object obj, Class<? extends Object> type, String version, URI actor,
      ZonedDateTime ts) {
    notify(obj, type, version, Operation.DELETED, actor, ts);
  }

  /**
   * Send notification that an entity was mutated in some way.
   *
   * @param obj representation of the object as it currently is
   * @param type type of the object
   * @param version version of the schema of the object
   * @param operation what was done to the entity (CREATED, UPDATED, DELETED)
   * @param actor who made the change
   * @param ts when was the change made
   */
  default void notify(Object obj, Class<? extends Object> type, String version,
      EntityLifecycleNotification.Operation operation, URI actor, ZonedDateTime ts) {
    notify(EntityLifecycleNotification.builder()
//        .typename(type.getTypeName())
        .version(version)
        .operation(operation)
        .timestamp(ts)
        .actor(actor)
        .entityDescriptor(EntityDescriptor.create(obj))
        .build());
  }

  void notify(EntityLifecycleNotification notification);
}
