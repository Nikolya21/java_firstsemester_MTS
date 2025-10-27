package com.mipt.nikolyakhachatryan.Homework1_8.classes;

public class Main {
  public static void main(String[] args) {
    final User user = new User();
    user.setName("A"); // очень короткое имя
    user.setEmail("invalid-email"); // невалидный email

    final ValidationResult result = Validator.validate(user);

    if (!result.isValid()) {
      System.out.println("Ошибки валидации:");
      result.getErrors().forEach(System.out::println);
    }
  }
}
