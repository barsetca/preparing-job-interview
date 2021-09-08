package com.cherniak.thymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableRetry
//@EnableScheduling
public class SpringThymeleafApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringThymeleafApplication.class, args);
  }

}
