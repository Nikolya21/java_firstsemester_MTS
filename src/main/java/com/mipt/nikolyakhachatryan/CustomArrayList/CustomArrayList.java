package com.mipt.nikolyakhachatryan.CustomArrayList;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.Iterable;
public class CustomArrayList<A> implements CustomList<A>, Iterable<A> {
  private Object[] massive;
  public static final double growthCapacity = 1.5;
  public static final int initial_capacity = 10;
  private int size;

  public CustomArrayList() {
    this.massive = new Object[initial_capacity];
    this.size = 0;
  }

  @Override
  public void add(A element) {
    if(element == null) {
      throw new IllegalArgumentException("Can not be that");
    }
    isExpandTheArray();
    massive[size++] = element;
  }

  @Override
  public A get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    return (A) massive[index];
  }

  @Override
  public A remove(int index) {
    if (index >= size || index < 0) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    A removed = (A) massive[index];
    System.arraycopy(massive,index + 1, massive, index,size - index - 1);
    massive[size--] = null;
    return removed;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }
  public void isExpandTheArray() {
    if (size == massive.length) {
      int newCapacity = (int) (massive.length * growthCapacity);
      Object[] newMassive = new Object[newCapacity];
      System.arraycopy(massive,0,newMassive,0,size);
      massive = newMassive;
    }
  }
  public Iterator<A> iterator() {
    return new CustomArrayListIterator();
  }
  private class CustomArrayListIterator implements Iterator<A> {
    private int currentIndex = 0;

    @Override
    public boolean hasNext() {
      return currentIndex < size;
    }

    public A next() {
      if(!hasNext()) {
        throw new NoSuchElementException();
      }
      return (A) massive[currentIndex++];
    }
  }
}
