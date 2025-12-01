package com.mipt.nikolyakhachatryan.Homework1_8.classes;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult{
  private boolean isValid = true;
  private List<String> errors = new ArrayList<>();

  public boolean isValid() {
    return isValid;
  }
  public List<String> getErrors() {
    return new ArrayList<>(errors);
  }
  public void addError(String message) {
    errors.add(message);
    isValid = false;
  }
}
