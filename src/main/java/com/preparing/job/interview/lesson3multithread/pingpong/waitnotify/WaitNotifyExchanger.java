package com.preparing.job.interview.lesson3multithread.pingpong.waitnotify;

public class WaitNotifyExchanger {

  private final String first;
  private final String second;
  private boolean flag = false;

  public WaitNotifyExchanger(String first, String second) {
    this.first = first;
    this.second = second;
  }

  synchronized void printSecond() {
    while (!flag) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println(second);
    try {
      Thread.sleep(1000);// задежка для вывода пары First-Second c интервалом в 1 секунду
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    flag = false;
    notify();
  }

  synchronized void printFirst() {
    while (flag) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.print(first + "-");
    flag = true;
    notify();
  }
}
