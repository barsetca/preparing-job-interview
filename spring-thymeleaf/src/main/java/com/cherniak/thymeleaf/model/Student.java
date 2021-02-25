package com.cherniak.thymeleaf.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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
  @Size(min = 2, max = 100)
  private String name;

  @Column(name = "age")
  @Min(5)
  @Max(100)
  private Integer age;

  public Student(String name, Integer age) {
    this(null, name, age);
  }

  public Student(Long id, String name, Integer age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }
}
