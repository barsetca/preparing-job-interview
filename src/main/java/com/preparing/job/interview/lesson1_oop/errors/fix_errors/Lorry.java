package com.preparing.job.interview.lesson1_oop.errors.fix_errors;

public class Lorry extends Car {

  @Override
  public void move() {
    System.out.println("Car Lorry s moving");
  }

  @Override
  public void stop() {
    System.out.println("Car Lorry is stop");
  }


  @Override
  void open() {
    System.out.println("Car Lorry is open");
  }
}
