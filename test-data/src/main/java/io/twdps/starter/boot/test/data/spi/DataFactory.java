package io.twdps.starter.boot.test.data.spi;

import java.util.List;

public interface DataFactory<T> {

  static String DEFAULT_NAME = "default";

  T getNamedData(String name);

  default T getData() {
    return getNamedData(DEFAULT_NAME);
  }

  default List<T> getNamedDataCollection(String name) {
    return List.of(this.getData());
  }

  default List<T> getDataCollection() {
    return getNamedDataCollection(DEFAULT_NAME);
  }
}
