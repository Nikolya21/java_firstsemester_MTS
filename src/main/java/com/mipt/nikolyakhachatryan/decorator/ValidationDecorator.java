package com.mipt.nikolyakhachatryan.decorator;

import java.util.Optional;

public class ValidationDecorator implements DataService {
  private final DataService delegate;

  public ValidationDecorator(DataService delegate) {
    this.delegate = delegate;
  }

  private void validateKey(String key) {
    if (key == null || key.trim().isEmpty()) {
      throw new IllegalArgumentException("Key must not be null or blank");
    }
  }

  private void validateData(String data) {
    if (data == null) {
      throw new IllegalArgumentException("Data must not be null");
    }
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    validateKey(key);
    return delegate.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    validateKey(key);
    validateData(data);
    delegate.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    validateKey(key);
    return delegate.deleteData(key);
  }
}