package com.preparing.job.interview.lesson1_oop.errors.src_errors;

// не реализует интерфейс Stoppable  - не доступен метод stop()
// рекомендуется реализовать
public class LightWeightCar extends Car implements Movable {

  @Override
  void open() {
    System.out.println("Car is open");
  }

  @Override
  public void move() {
    System.out.println("Car is moving");
  }
}
