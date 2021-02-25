package com.cherniak.thymeleaf.web;

import com.cherniak.thymeleaf.model.Student;
import com.cherniak.thymeleaf.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;

  @GetMapping
  public String findAll(Model model) {
    List<Student> students = studentService.findAll();
    model.addAttribute("students", students);

    return "students";
  }

  @PostMapping
  public String createOrUpdate(@ModelAttribute Student student) {
    studentService.createOrUpdate(student);
    return "redirect:/students";
  }

  @GetMapping("/{id}")
  public String delete(@PathVariable Long id){
    studentService.delete(id);
    return "redirect:/students";
  }

}




