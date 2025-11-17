package com.mipt.nikolyakhachatryan.decorator;

import com.mipt.nikolyakhachatryan.decorator.repository.DataService;

import java.util.Optional;

public class LoggingDecorator implements DataService {
  private final DataService delegate;

  public LoggingDecorator(DataService delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    System.out.println("[LOG] findDataByKey: " + key);
    return delegate.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    System.out.println("[LOG] saveData: key=" + key + ", data=" + data);
    delegate.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    System.out.println("[LOG] deleteData: " + key);
    return delegate.deleteData(key);
  }
}