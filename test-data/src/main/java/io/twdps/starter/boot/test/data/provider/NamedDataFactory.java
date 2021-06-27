package io.twdps.starter.boot.test.data.provider;

import io.twdps.starter.boot.test.data.spi.DataFactory;
import io.twdps.starter.boot.test.data.spi.DataLoader;
import io.twdps.starter.boot.test.data.spi.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class NamedDataFactory<T> implements DataFactory<String, T> {

  Map<String, T> data;
  Map<String, List<T>> collections;
  public static String DEFAULT_SPEC = "default";

  public NamedDataFactory(DataLoader<T> loader) {
    this.data = loader.loadData();
    this.collections = loader.loadCollections();
  }

  @Override
  public T createBySpec(String name) {
    if (data.containsKey(name)) {
      return data.get(name);
    } else {
      throw new DataNotFoundException(name);
    }
  }

  @Override
  public T create() {
    return createBySpec(DEFAULT_SPEC);
  }

  @Override
  public List<T> createCollectionBySpec(String name) {
    if (collections.containsKey(name)) {
      return collections.get(name);
    } else {
      throw new DataNotFoundException(name);
    }
  }

  @Override
  public List<T> createCollection() {
    return createCollectionBySpec(DEFAULT_SPEC);
  }
}
