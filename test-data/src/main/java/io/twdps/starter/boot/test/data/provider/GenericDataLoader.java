package io.twdps.starter.boot.test.data.provider;

import io.twdps.starter.boot.test.data.spi.DataLoader;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
public class GenericDataLoader<T> implements DataLoader<T> {
  private final Map<String, T> data = new TreeMap<>();
  private final Map<String, List<T>> collections = new TreeMap<>();

  @Override
  public Map<String, T> loadData() {
    return data;
  }

  @Override
  public Map<String, List<T>> loadCollections() {
    return collections;
  }
}
