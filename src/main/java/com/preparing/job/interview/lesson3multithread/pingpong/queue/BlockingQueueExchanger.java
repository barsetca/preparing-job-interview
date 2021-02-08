package com.preparing.job.interview.lesson3multithread.pingpong.queue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueExchanger {

  private final String first;
  private final String second;

  Queue<String> singleQueue = new ArrayBlockingQueue<>(2);

  public BlockingQueueExchanger(String first, String second) {
    this.first = first+"-";
    this.second = second + "\n";
  }

  public void printSecond() {
    String toPrint = singleQueue.poll();
    if (toPrint != null) {
      System.out.print(toPrint);
    }
  }

  public void printFirst() {
    singleQueue.offer(first);
    singleQueue.offer(second);
    try {
      TimeUnit.SECONDS.sleep(1);// задежка для вывода пары First-Second c интервалом в 1 секунду
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
