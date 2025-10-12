package ru.mipt.hw1_6;

public class ArrayUtils {
  public static <T> int findFirst(T[] array, T element) {
    if (array == null) {
      return -1;
    } else {
      for (int i = 0; i < array.length; i++) {
        if (array[i] == null && element == null) {
          return i;
        } else {
          if (element.equals(array[i])) {
            return i;
          }
        }
      }
    }
    return -1;
  }
  public static void main(String[] args) {
    final String[] names = {"Alice", "Bob", "Charlie"};
    final int index = ArrayUtils.findFirst(names, "Bob"); // Ожидаем: 1 (тк нумерация в массиве начинается с нуля)
  }
}
