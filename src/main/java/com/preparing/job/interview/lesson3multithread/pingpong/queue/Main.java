package com.preparing.job.interview.lesson3multithread.pingpong.queue;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    BlockingQueueExchanger exchanger = new BlockingQueueExchanger("Ping", "Pong");

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
