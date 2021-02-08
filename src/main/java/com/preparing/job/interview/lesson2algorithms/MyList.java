package com.preparing.job.interview.lesson2algorithms;

public interface MyList<T> {

  int size();

  T get(int index);

  T remove(int index);

  boolean add(T element);

  void clear();

  boolean contains(T element);

  boolean insert(int index, T element);

  int indexOf(T element);

  int getCapacity();

}
