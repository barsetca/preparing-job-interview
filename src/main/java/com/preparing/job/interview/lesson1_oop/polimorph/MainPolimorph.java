package com.preparing.job.interview.lesson1_oop.polimorph;

public class MainPolimorph {

  public static void main(String[] args) {
    Figure circle = new Circle();
    Figure square = new Square();
    Figure triangle = new Triangle();
    circle.drawFigure();
    square.drawFigure();
    triangle.drawFigure();

    draw(circle);
    draw(square);
    draw(triangle);
  }

  public static void draw(Figure figure) {
    figure.drawFigure();
  }
}
