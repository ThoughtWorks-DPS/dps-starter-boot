package io.twdps.starter.boot.notifier.lifecycle.entity.model;

public class Foo {

  public String data;

  public Foo() {
    this("default");
  }

  public Foo(String data) {
    this.data = data;
  }
}
