package com.preparing.job.interview.lesson1_oop.errors.fix_errors;

public class Main {

  public static void main(String[] args) {
    Car lorry = new Lorry();
    Car light = new LightWeightCar();
    lorry.setColor("red");
    lorry.setName("lorry");
    System.out.println(lorry);
    lorry.open();
    lorry.start();
    lorry.move();
    lorry.stop();

    light.setColor("blue");
    light.setName("light");
    System.out.println(light);
    light.open();
    light.start();
    light.move();
    light.stop();
  }
}
