package com.preparing.job.interview.lesson3multithread.pingpong.reentrantlock;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    ReentrantLockExchanger exchanger = new ReentrantLockExchanger("Ping", "Pong");

    new Thread(() -> {
      while (true) {
        exchanger.printFirst();
      }
    }).start();

    new Thread(() -> {
      while (true) {
        exchanger.printSecond();
      }
    }).start();
  }
}
