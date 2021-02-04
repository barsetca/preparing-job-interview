package com.preparing.job.interview.lesson2algorithms;

public class MyLinkedList<T> implements MyList<T> {

  private int size;
  Node<T> head;

  @Override
  public int size() {
    return size;
  }

  @Override
  public T get(int index) {
    int innerIndex = size - 1 - index;
    int currentInnerIndex = 0;
    Node<T> current = head;
    while (current != null) {
      if (currentInnerIndex == innerIndex) {
        return current.value;
      }
      current = current.next;
      currentInnerIndex++;
    }
    return null;
  }

  @Override
  public T remove(int index) {
    if (size == 0) {
      return null;
    }
    int innerIndex = size - 1 - index;
    Node<T> current = head;
    Node<T> previous = null;
    int currentInnerIndex = 0;
    while (current != null) {
      if (currentInnerIndex == innerIndex) {
        T removed = current.value;
        if (currentInnerIndex == 0) {
          head = current.next;
          size--;
          return removed;
        }
        previous.next = previous.next.next;
        size--;
        return removed;
      }
      previous = current;
      current = current.next;
      currentInnerIndex++;
    }
    return null;
  }

  @Override
  public boolean add(T element) {
    head = new Node<>(head, element);
    size++;
    return true;
  }

  @Override
  public void clear() {
    Node<T> current = head;
    Node<T> previous;
    while (current != null) {
      previous = current.next;
      current.next = null;
      current.value = null;
      current = previous;
    }
    head = null;
    size = 0;
  }

  @Override
  public boolean contains(T element) {
    Node<T> current = head;
    while (current != null) {
      if (current.value.equals(element)) {
        return true;
      }
      current = current.next;
    }
    return false;
  }

  @Override
  public boolean insert(int index, T element) {
    checkIndex(index);
    if (size == 0) {
      return add(element);
    }
    int innerIndex = size - index;
    Node<T> current = head;
    Node<T> previous = null;
    int currentInnerIndex = 0;
    while (current != null) {
      if (currentInnerIndex == innerIndex) {
        if (currentInnerIndex == 0) {
          return add(element);
        }
        previous.next = new Node<>(current, element);
        size++;
        return true;
      }
      previous = current;
      current = current.next;
      currentInnerIndex++;
      if (currentInnerIndex == size) {
        previous.next = new Node<>(current, element);
        size++;
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOf(T element) {
    Node<T> current = head;
    int index = 0;
    while (current != null) {
      if (current.value.equals(element)) {
        return size - 1 - index;
      }
      current = current.next;
      index++;
    }
    return -1;
  }

  @Override
  public int getCapacity() {
    return 0;
  }

  @Override
  public String toString() {
    Node<T> current = head;
    String[] array = new String[size];
    for (int i = size - 1; i >= 0; i--) {
      array[i] = String.valueOf(current.value);
      current = current.next;
    }
    String string = String.join(", ", array);
    return "MyLinkedList{" + string + "}";
  }

  static class Node<T> {

    Node<T> next;
    T value;

    public Node(Node<T> next, T value) {
      this.next = next;
      this.value = value;
    }
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
  }
}
