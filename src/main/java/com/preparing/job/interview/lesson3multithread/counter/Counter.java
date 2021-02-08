package com.preparing.job.interview.lesson3multithread.counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

  private long counter = 0L;
  private final Lock lock = new ReentrantLock();

  public void increaseCounter() {
    lock.lock();
    counter++;
    lock.unlock();
  }

  public long getCounter() {
    return counter;
  }

}
