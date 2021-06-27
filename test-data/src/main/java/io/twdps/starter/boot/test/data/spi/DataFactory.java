package io.twdps.starter.boot.test.data.spi;

import java.util.List;

public interface DataFactory<U, T> {

  T createBySpec(U spec) throws DataNotFoundException;

  T create();

  default List<T> createCollectionBySpec(U spec) throws DataNotFoundException {
    return List.of(this.createBySpec(spec));
  }

  List<T> createCollection();
}
