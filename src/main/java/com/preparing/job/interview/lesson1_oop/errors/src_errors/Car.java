package com.preparing.job.interview.lesson1_oop.errors.src_errors;

/*
не реализует интерфейсы Stoppable и Movable наследники при создании Car lorry = new ...
не доступны методы реализованных интерфейсов
рекомендуется имплементировать данные интерфесф а у наследников убратьБ рекомендумый вариант в пакете fix_errors
 */

abstract class Car {

  //рекомендуется использовать единые области видимости и инкопсуляция - изменить на приват
  // в исходнике дз не добавлен класс Engine
//     public Engine engine;
  private String color;
  private String name;

  //рекомендуется использовать единые области видимости - здесь,например, изменить на default
  protected void start() {
    System.out.println("Car starting");
  }

  abstract void open();

  public Engine getEngine() {
    return engine;
  }

  public void setEngine(Engine engine) {
    this.engine = engine;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Car{" +
        "engine=" + engine +
        ", color='" + color + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
