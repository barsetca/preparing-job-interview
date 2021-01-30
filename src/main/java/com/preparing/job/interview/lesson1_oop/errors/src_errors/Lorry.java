package com.preparing.job.interview.lesson1_oop.errors.src_errors;

//в исходнике д/з - ошибка: интерфейсы не могут быть extends, д.б. implements
//в исходнике д/з не реализован метод open() абстрактного класса  Car
public class Lorry extends Car implements Movable, Stoppable {

  // рекомендуется использовать @Override
  public void move() {
    System.out.println("Car is moving");
  }

  // рекомендуется использовать @Override
  public void stop() {
    System.out.println("Car is stop");
  }
}
