package com.preparing.job.interview.lesson5hibernate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Data
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  public Student(String name) {
    this(null, name);
  }

  public Student(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
