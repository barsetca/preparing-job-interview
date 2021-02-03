package com.preparing.job.interview.lesson1_oop.errors.fix_errors;

abstract class Car implements Movable, Stoppable {

  private Engine engine;
  private String color;
  private String name;

  void start() {
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

class Engine {

}
