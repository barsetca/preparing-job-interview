package com.preparing.job.interview.lesson3multithread.counter;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Counter counter = new Counter();
    for (int i = 0; i < 200; i++) {
      Thread thread = new Thread(() -> {
        for (int j = 0; j < 1000; j++) {
          counter.increaseCounter();
        }
      });
      thread.start();
    }
    Thread.sleep(1000);

    System.out.println("Counter:" + counter.getCounter());
  }

}
