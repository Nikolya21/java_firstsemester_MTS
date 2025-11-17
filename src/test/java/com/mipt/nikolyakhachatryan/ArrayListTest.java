package com.mipt.nikolyakhachatryan;

import com.mipt.nikolyakhachatryan.CustomArrayList.CustomArrayList;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {

  private CustomArrayList<String> list;

  @BeforeEach
  void setUp() {
    list = new CustomArrayList<>();
  }

  @Test
  void testBasicOperations() {
    assertTrue(list.isEmpty());
    assertEquals(0, list.size());

    list.add("A");
    list.add("B");
    assertEquals(2, list.size());
    assertFalse(list.isEmpty());
    assertEquals("A", list.get(0));
    assertEquals("B", list.get(1));

    assertEquals("A", list.remove(0));
    assertEquals(1, list.size());
    assertEquals("B", list.get(0));
  }

  @Test
  void testExceptions() {
    assertThrows(IllegalArgumentException.class, () -> list.add(null));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
  }

  @Test
  void testIterator() {
    list.add("X");
    list.add("Y");

    var it = list.iterator();
    assertTrue(it.hasNext());
    assertEquals("X", it.next());
    assertTrue(it.hasNext());
    assertEquals("Y", it.next());
    assertFalse(it.hasNext());
  }
}