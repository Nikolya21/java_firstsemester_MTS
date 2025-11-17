package com.mipt.nikolyakhachatryan.decorator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CachingDecorator implements DataService {
  private final DataService delegate;
  private final Map<String, Optional<String>> cache = new HashMap<>();

  public CachingDecorator(DataService delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    return cache.computeIfAbsent(key, k -> delegate.findDataByKey(k));
  }

  @Override
  public void saveData(String key, String data) {
    delegate.saveData(key, data);
    cache.put(key, Optional.of(data));  // обновляем кэш
  }

  @Override
  public boolean deleteData(String key) {
    boolean removed = delegate.deleteData(key);
    cache.remove(key);  // инвалидируем кэш
    return removed;
  }
}