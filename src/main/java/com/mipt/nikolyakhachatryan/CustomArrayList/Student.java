package com.mipt.nikolyakhachatryan.CustomArrayList;

import java.util.*;

public class Student {
  private final int id;
  private final String name;
  private final double grade;

  public Student(int id, String name, double grade) {
    this.id = id;
    this.name = name;
    this.grade = grade;
  }

  public int getId() { return id; }
  public String getName() { return name; }
  public double getGrade() { return grade; }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || getClass() != obj.getClass()) return false;
    Student student = (Student) obj;
    return id == student.id && Objects.equals(name, student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  public List<Student> findStudentsByGradeRange(Map<Integer, Student> map, double minGrade, double maxGrade) {
    List<Student> students = new ArrayList<>();
    for (Student student : map.values()) {
      if (student.getGrade() >= minGrade && student.getGrade() <= maxGrade) {
        students.add(student);
      }
    }
    return students;
  }

  public List<Student> getTopNStudents(TreeMap<Integer, Student> map, int n) {
    List<Student> students = new ArrayList<>(map.values());
    students.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
    return students.stream().limit(n).toList();
  }
}