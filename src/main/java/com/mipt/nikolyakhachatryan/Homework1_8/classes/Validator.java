package com.mipt.nikolyakhachatryan.Homework1_8.classes;
import com.mipt.nikolyakhachatryan.Homework1_8.interfase.Email;
import com.mipt.nikolyakhachatryan.Homework1_8.interfase.NotNull;
import com.mipt.nikolyakhachatryan.Homework1_8.interfase.Range;
import com.mipt.nikolyakhachatryan.Homework1_8.interfase.Size;
import java.lang.reflect.Field;

public class Validator {

  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);

      try {
        Object value = field.get(object);
        if (field.isAnnotationPresent(NotNull.class)) {
          NotNull ann = field.getAnnotation(NotNull.class);
          if (value == null) {
            result.addError(ann.message());
          }
        }
        if (field.isAnnotationPresent(Size.class)) {
          Size ann = field.getAnnotation(Size.class);
          if (value instanceof String str) {
            if (str.length() < ann.min() || str.length() > ann.max()) {
              result.addError(ann.message());
            }
          }
        }
        if (field.isAnnotationPresent(Range.class)) {
          Range ann = field.getAnnotation(Range.class);
          if (value instanceof Integer num) {
            if (num < ann.min() || num > ann.max()) {
              result.addError(ann.message());
            }
          }
        }
        if (field.isAnnotationPresent(Email.class)) {
          Email ann = field.getAnnotation(Email.class);
          if (value instanceof String str) {
            if (!str.matches(EMAIL_REGEX)) {
              result.addError(ann.message());
            }
          } else if (value != null) {
            result.addError(ann.message());
          }
        }

      } catch (IllegalAccessException e) {
        throw new RuntimeException("Ошибка доступа к полю: " + field.getName(), e);
      }
    }

    return result;
  }
}