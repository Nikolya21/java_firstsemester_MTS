package com.mipt.nikolyakhachatryan.CustomArrayList;

public interface CustomList<A> {
  void add(A element);
  A get(int index);
  A remove(int index);
  int size();
  boolean isEmpty();
}
