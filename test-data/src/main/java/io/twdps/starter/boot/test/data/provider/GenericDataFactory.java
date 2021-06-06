package io.twdps.starter.boot.test.data.provider;

import io.twdps.starter.boot.test.data.spi.DataFactory;
import io.twdps.starter.boot.test.data.spi.DataLoader;
import io.twdps.starter.boot.test.data.spi.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GenericDataFactory<T> implements DataFactory<T> {

  Map<String, T> data;
  Map<String, List<T>> collections;

  public GenericDataFactory(DataLoader<T> loader) {
    this.data = loader.loadData();
    this.collections = loader.loadCollections();
  }

  @Override
  public T getNamedData(String name) {
    if (data.containsKey(name)) {
      return data.get(name);
    } else {
      throw new DataNotFoundException(name);
    }
  }

  @Override
  public List<T> getNamedDataCollection(String name) {
    if (collections.containsKey(name)) {
      return collections.get(name);
    } else {
      throw new DataNotFoundException(name);
    }
  }
}
