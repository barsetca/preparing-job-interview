package com.cherniak.thymeleaf.alexander;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureTask {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    var isValid = new CompletableFuture<Boolean>();
    var string = new CompletableFuture<String>();
    var t = new Thread(() -> {
      try {
        Thread.sleep(1000);
        isValid.complete(true);
        string.complete("string");
        Thread.sleep(1000);
        System.out.println("Processing completed");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    t.start();
    System.out.println("Valid: " + isValid.join());
    System.out.println("Return: " + string.get());
    t.join();
  }
}
