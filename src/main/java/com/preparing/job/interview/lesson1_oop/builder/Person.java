package com.preparing.job.interview.lesson1_oop.builder;

public class Person {

  private final String firstName;
  private final String lastName;
  private final String middleName;
  private final String country;
  private final String address;
  private final String phone;
  private final int age;
  private final String gender;

  private Person(Builder builder){
    firstName = builder.firstName;
    lastName = builder.lastName;
    middleName = builder.middleName;
    country = builder.country;
    address = builder.address;
    phone = builder.phone;
    age = builder.age;
    gender = builder.gender;
  }

  public static class Builder {

    private String firstName;
    private String lastName;
    private String middleName;
    private String country;
    private String address;
    private String phone;
    private int age;
    private String gender;

    public Builder(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
    }


    public Builder addMiddleName(String middleName) {
      this.middleName = middleName;
      return this;
    }

    public Builder addCountry(String country) {
      this.country = country;
      return this;
    }

    public Builder addAddress(String address) {
      this.address = address;
      return this;
    }

    public Builder addPhone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder addAge(int age) {
      this.age = age;
      return this;
    }

    public Builder addGender(String gender) {
      this.gender = gender;
      return this;
    }

   public Person build(){
      return new Person(this);
   }
  }

  @Override
  public String toString() {
    return "Person{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", middleName='" + middleName + '\'' +
        ", country='" + country + '\'' +
        ", address='" + address + '\'' +
        ", phone='" + phone + '\'' +
        ", age=" + age +
        ", gender='" + gender + '\'' +
        '}';
  }
}
