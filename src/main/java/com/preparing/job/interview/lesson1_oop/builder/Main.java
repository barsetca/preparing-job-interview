package com.preparing.job.interview.lesson1_oop.builder;

import com.preparing.job.interview.lesson1_oop.builder.Person.Builder;

public class Main {

  public static void main(String[] args) {
    Person person = new Person.Builder("Bob", "Dark")
        .addAge(50)
        .addGender("M")
        .addAddress("King street")
        .addCountry("USA")
        .addPhone("+3445 5698746")
        .build();
    System.out.println(person);
  }

}

