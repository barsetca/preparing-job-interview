package com.cherniak.thymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@EnableScheduling
public class SpringThymeleafApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringThymeleafApplication.class, args);
  }

}
