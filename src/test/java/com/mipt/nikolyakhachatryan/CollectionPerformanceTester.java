package com.mipt.nikolyakhachatryan;

import java.util.*;

public class CollectionPerformanceTester {

  private static final int N = 10_000;

  public static void main(String[] args) {
    System.out.println("Операция\t\t\tArrayList (мс)\tLinkedList (мс)");


    testAddToEnd(new ArrayList<>(), "Добавление в конец");
    testAddToEnd(new LinkedList<>(), "Добавление в конец");

    testAddToFront(new ArrayList<>(), "Добавление в начало");
    testAddToFront(new LinkedList<>(), "Добавление в начало");

    testInsertMiddle(new ArrayList<>(), "Вставка в середину");
    testInsertMiddle(new LinkedList<>(), "Вставка в середину");

    testGetByIndex(new ArrayList<>(), "Доступ по индексу");
    testGetByIndex(new LinkedList<>(), "Доступ по индексу");

    testRemoveFromFront(new ArrayList<>(), "Удаление из начала");
    testRemoveFromFront(new LinkedList<>(), "Удаление из начала");

    testRemoveFromEnd(new ArrayList<>(), "Удаление из конца");
    testRemoveFromEnd(new LinkedList<>(), "Удаление из конца");
  }

  private static void testAddToEnd(List<Integer> list, String operation) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      list.add(i);
    }
    long time = System.currentTimeMillis() - start;
    printResult(operation, time, list instanceof ArrayList);
  }

  private static void testAddToFront(List<Integer> list, String operation) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      list.add(0, i);
    }
    long time = System.currentTimeMillis() - start;
    printResult(operation, time, list instanceof ArrayList);
  }

  private static void testInsertMiddle(List<Integer> list, String operation) {
    for (int i = 0; i < N; i++) {
      list.add(i);
    }
    long start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      list.add(N / 2, i);
    }
    long time = System.currentTimeMillis() - start;
    printResult(operation, time, list instanceof ArrayList);
  }

  private static void testGetByIndex(List<Integer> list, String operation) {
    for (int i = 0; i < N; i++) {
      list.add(i);
    }
    long start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      list.get(i);
    }
    long time = System.currentTimeMillis() - start;
    printResult(operation, time, list instanceof ArrayList);
  }

  private static void testRemoveFromFront(List<Integer> list, String operation) {
    for (int i = 0; i < N; i++) {
      list.add(i);
    }
    long start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      list.remove(0);
    }
    long time = System.currentTimeMillis() - start;
    printResult(operation, time, list instanceof ArrayList);
  }

  private static void testRemoveFromEnd(List<Integer> list, String operation) {
    for (int i = 0; i < N; i++) {
      list.add(i);
    }
    long start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      list.remove(list.size() - 1);
    }
    long time = System.currentTimeMillis() - start;
    printResult(operation, time, list instanceof ArrayList);
  }

  private static void printResult(String operation, long time, boolean isArrayList) {
    if (isArrayList) {
      System.out.printf("%-25s %-15d %-15s%n", operation, time, "-");
    } else {
      System.out.printf("%-25s %-15s %-15d%n", "-", "-", time);
    }
  }
}