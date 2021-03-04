package com.cherniak.thymeleaf.alexander;

import java.util.concurrent.CompletableFuture;

public class FutureTask {
  public static void main(String[] args) throws InterruptedException {
    var isValid = new CompletableFuture<Boolean>();
    var t = new Thread(() -> {
      try {
        Thread.sleep(1000);
        isValid.complete(true);
        Thread.sleep(1000);
        System.out.println("Processing completed");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    t.start();
    System.out.println("Valid: " + isValid.join());
    t.join();
  }
}
