package com.preparing.job.interview.lesson1_oop.errors.fix_errors;

public class LightWeightCar extends Car {

  @Override
  void open() {
    System.out.println("Car LightWeight is open");
  }

  @Override
  public void move() {
    System.out.println("Car LightWeightCar is moving");
  }
}
