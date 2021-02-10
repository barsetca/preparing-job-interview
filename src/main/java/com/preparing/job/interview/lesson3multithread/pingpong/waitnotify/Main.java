package com.preparing.job.interview.lesson3multithread.pingpong.waitnotify;

public class Main {

  public static void main(String[] args) {
    WaitNotifyExchanger exchanger = new WaitNotifyExchanger("Ping", "Pong");

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
