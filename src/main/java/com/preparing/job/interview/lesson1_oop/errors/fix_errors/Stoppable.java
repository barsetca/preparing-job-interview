package com.preparing.job.interview.lesson1_oop.errors.fix_errors;

public interface Stoppable {

  default void stop() {
    System.out.println("Car is stop");
  }
}


