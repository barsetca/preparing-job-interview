package com.cherniak.thymeleaf.service;

import com.cherniak.thymeleaf.model.Student;
import com.cherniak.thymeleaf.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository studentRepository;

  private static final Sort SORT_NAME = Sort.by(Sort.Order.asc("name"));

  public List<Student> findAll() {
    return studentRepository.findAll(SORT_NAME);
  }

  public Student get(Long id) throws NotFoundException {
    return studentRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  @Transactional
  public Student createOrUpdate(Student student) {
    return studentRepository.save(student);
  }

  @Transactional
  public void delete(Long id) {
    studentRepository.deleteById(id);
  }
}
