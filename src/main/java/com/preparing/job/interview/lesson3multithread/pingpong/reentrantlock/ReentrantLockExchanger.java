package com.preparing.job.interview.lesson3multithread.pingpong.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExchanger {

  private final String first;
  private final String second;
  private final Lock lock = new ReentrantLock();
  AtomicBoolean atomicBoolean = new AtomicBoolean();

  public ReentrantLockExchanger(String first, String second) {
    this.first = first;
    this.second = second;
  }

  public void printSecond() {
    while (atomicBoolean.get()) {
      if (lock.tryLock()) {
        try {
          System.out.println(second);
        } finally {
          try {
            TimeUnit.SECONDS
                .sleep(1); // задежка для вывода пары First-Second c интервалом в 1 секунду
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          atomicBoolean.set(false);
          lock.unlock();
        }
      }
    }
  }

  public void printFirst() {
    while (!atomicBoolean.get()) {
      if (lock.tryLock()) {
        try {
          System.out.print(first + "-");
        } finally {
          atomicBoolean.set(true);
          lock.unlock();
        }
      }
    }
  }
}
