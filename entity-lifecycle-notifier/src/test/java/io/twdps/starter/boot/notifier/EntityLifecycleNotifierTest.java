package io.twdps.starter.boot.notifier;

import io.twdps.starter.boot.notifier.EntityLifecycleNotification.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityLifecycleNotifierTest {

  class MemoizedEntityLifecycleNotifier implements EntityLifecycleNotifier {

    private EntityLifecycleNotification last;
    private TimestampProvider provider;

    public MemoizedEntityLifecycleNotifier(TimestampProvider provider) {
      this.provider = provider;
    }

    public void notify(EntityLifecycleNotification notification) {
      last = notification;
    }

    public TimestampProvider getTimestampProvider() {
      return this.provider;
    }

    public EntityLifecycleNotification getLast() {
      return last;
    }
  }

  class Foo {

    public String data;

    public Foo(String data) {
      this.data = data;
    }
  }

  private ZonedDateTime now = ZonedDateTime.now();
  private URI user = URI.create("user:uuid");
  private String version = "0.0.1";
  private Foo entity = new Foo("foo");

  MemoizedEntityLifecycleNotifier notifier;
  EntityLifecycleNotification notification = EntityLifecycleNotification.builder()
      .timestamp(now)
      .actor(user)
      .version(version)
      .entityDescriptor(EntityDescriptor.create(entity))
      .operation(Operation.CREATED)
      .build();

  @BeforeEach
  public void setup() {
    notifier = new MemoizedEntityLifecycleNotifier(new MemoizedTimestampProvider(now));
  }

  private void verify(EntityLifecycleNotification obj) {
    assertThat(obj.getActor()).isEqualTo(user);
    assertThat(obj.getEntityDescriptor()
        .getEntity()).isEqualTo(entity);
    assertThat(obj.getTimestamp()).isEqualTo(now);
    assertThat(obj.getVersion()).isEqualTo(version);

  }

  @Test
  public void notifyIsCalled() {
    notifier.notify(notification);
    EntityLifecycleNotification sent = notifier.getLast();
    verify(sent);
  }

  @Test
  public void notifyIsCalledWhenCreated() {
    notifier.created(entity, version, user);
    EntityLifecycleNotification sent = notifier.getLast();
    verify(sent);
  }

  @Test
  public void notifyIsCalledWhenUpdated() {
    notifier.updated(entity, version, user);
    EntityLifecycleNotification sent = notifier.getLast();
    verify(sent);
  }

  @Test
  public void notifyIsCalledWhenDeleted() {
    notifier.deleted(entity, version, user);
    EntityLifecycleNotification sent = notifier.getLast();
    verify(sent);
  }

  @Test
  public void notifyIsCalledWhenCreatedLong() {
    notifier.created(entity, entity.getClass(), version, user, now);
    EntityLifecycleNotification sent = notifier.getLast();
    verify(sent);
  }

  @Test
  public void notifyIsCalledWhenUpdatedLong() {
    notifier.updated(entity, entity.getClass(), version, user, now);
    EntityLifecycleNotification sent = notifier.getLast();
    verify(sent);
  }

  @Test
  public void notifyIsCalledWhenDeletedLong() {
    notifier.deleted(entity, entity.getClass(), version, user, now);
    EntityLifecycleNotification sent = notifier.getLast();
    verify(sent);
  }

}
