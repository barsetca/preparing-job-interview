package com.preparing.job.interview.lesson2algorithms;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MyArrayList<T> implements MyList<T> {

  private static final int DEFAULT_CAPACITY = 8;

  private T[] array;
  private int size;
  private int initialCapacity;

  public MyArrayList() {
    this(DEFAULT_CAPACITY);
  }

  @SuppressWarnings("unchecked")
  public MyArrayList(int initialCapacity) {
    if (initialCapacity < 1) {
      initialCapacity = 1;
    }
    this.initialCapacity = initialCapacity;
    this.array = (T[]) new Object[initialCapacity];
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public T get(int index) {
    checkIndex(index);
    return array[index];
  }

  @Override
  public T remove(int index) {
    checkIndex(index);
    T removed = array[index];
    if (size - 1 - index >= 0)
      System.arraycopy(array, index + 1, array, index, size - 1 - index);
    array[size - 1] = null;
    size--;
    return removed;
  }

  @Override
  public boolean add(T element) {
    checkForResize();
    array[size++] = element;
    return true;
  }

  @Override
  public void clear() {
    Arrays.fill(array, null);
    size = 0;
  }

  @Override
  public boolean contains(T element) {
    return indexOf(element) >= 0;
  }

  @Override
  public boolean insert(int index, T element) {
    if (index == size) {
      return add(element);
    }
    checkIndex(index);
    checkForResize();
    IntStream.iterate(size, i -> i > index, i -> i - 1).forEach(i -> array[i] = array[i - 1]);
    array[index] = element;
    size++;
    return true;
  }

  @Override
  public int indexOf(T element) {
    for (int i = 0; i < size; i++) {
      if (array[i].equals(element)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int getCapacity() {
    return initialCapacity;
  }

  private void checkForResize() {
    if (size == array.length) {
      resize();
    }
  }

  private void resize() {
    initialCapacity = initialCapacity * 2 + 1;
    array = Arrays.copyOf(array, initialCapacity);
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("MyArrayList{");

    for (int i = 0; i < size - 1; i++) {
      builder.append(array[i]).append(", ");
    }
    builder.append(array[size - 1]).append("}");
    return builder.toString();
  }
}
