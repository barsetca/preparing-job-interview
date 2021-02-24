package com.preparing.job.interview.web;

import com.preparing.job.interview.lesson5hibernate.Student;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class SimpleController {

  private static List<Student> students = new ArrayList<>(Arrays.asList(new Student(1L, "Bill"), new Student(2L,"Bob")));

  @GetMapping("/{id}")
  public Student get(@PathVariable Long id) {
    Student studentForGet = null;
    for (Student student : students) {
      if (student.getId().equals(id)) {
        studentForGet = student;
      }
    }
    return studentForGet;
  }

  @GetMapping
  public List<Student> getAll() {
      return students;
  }


  @PostMapping()
  public Student create(@RequestParam Long id, @RequestParam String name) {
    Student newStudent = new Student(id, name);
    if (students.contains(newStudent)){
      return null;
    }
    students.add(newStudent);
    return newStudent;
  }

  @PostMapping("/body")
  public Student createFromBody(@RequestBody Student student) {
    if (students.contains(student)){
      return null;
    }
    students.add(student);
    return student;
  }

}
