package com.preparing.job.interview.lesson5hibernate;

import java.util.List;

public class Main {

  public static void main(String[] args) {

    /////////////////////////Student////////////////////////////////
    Dao<Student, Long> studentLongDao = new Dao<>(Student.class);

    Student student = studentLongDao.create(new Student("Name"));
    System.out.println(student); // Student(id=1, name=Name)
    Student studentGet = studentLongDao.getById(1L);
    System.out.println("studentGet " + studentGet); // studentGet Student(id=1, name=Name)

    student.setName("newName");
    System.out.println("studentUpdate " + student); // studentUpdate Student(id=1, name=newName)

    List<Student> students = studentLongDao.getAll();
    System.out.println(students); // [Student(id=1, name=newName)]
    System.out.println(students.size()); // 1

    studentLongDao.create(new Student("Name"));
    List<Student> students2 = studentLongDao.getAll();
    System.out.println(students2); // [Student(id=1, name=newName), Student(id=2, name=Name)]
    System.out.println(students2.size()); // 2

    studentLongDao.delete(student);
    List<Student> students3 = studentLongDao.getAll();
    System.out.println(students3); // [Student(id=2, name=Name)]
    System.out.println(students3.size()); //1

    /////////////////////////Teacher////////////////////////////////
    Dao<Teacher, Long> teacherLongDao = new Dao<>(Teacher.class);

    Teacher teacher = teacherLongDao.create(new Teacher("Teacher"));
    System.out.println(teacher); // Teacher(id=1, name=Teacher)
    Teacher teacherGet = teacherLongDao.getById(1L);
    System.out.println("teacherGet " + teacherGet); // teacherGet Teacher(id=1, name=Teacher)

    teacher.setName("newTeacher");
    teacherLongDao.update(teacher);
    System.out.println("teacherUpdate " + teacher); //teacherUpdate Teacher(id=1, name=newTeacher)

    List<Teacher> teachers = teacherLongDao.getAll();
    System.out.println(teachers); // [Teacher(id=1, name=newTeacher)]
    System.out.println(teachers.size()); // 1

    teacherLongDao.create(new Teacher("Teacher"));
    List<Teacher> teachers2 = teacherLongDao.getAll();
    System.out.println(teachers2); //[Teacher(id=1, name=newTeacher), Teacher(id=2, name=Teacher)]
    System.out.println(teachers2.size()); // 2

    teacherLongDao.delete(teacher);
    List<Teacher> teachers3 = teacherLongDao.getAll();
    System.out.println(teachers3); // [Teacher(id=2, name=Teacher)]
    System.out.println(teachers3.size()); //1
  }
}




