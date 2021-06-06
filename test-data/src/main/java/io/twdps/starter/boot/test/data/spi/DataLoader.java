package io.twdps.starter.boot.test.data.spi;

import java.util.List;
import java.util.Map;

public interface DataLoader<T> {
  Map<String, T> loadData();

  Map<String, List<T>> loadCollections();
}
