package com.preparing.job.interview.lesson2algorithms;

import com.preparing.job.interview.lesson2algorithms.MyLinkedList.Node;

public class MyLinkedList2<T> implements MyList<T> {

  private int size;
  Node<T> head;
  Node<T> tail;

  @Override
  public int size() {
    return size;
  }

  @Override
  public T get(int index) {
    int currentInnerIndex = 0;
    Node<T> current = tail;
    while (current != null) {
      if (currentInnerIndex == index) {
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
    Node<T> current = tail;
    Node<T> previous = null;
    int currentInnerIndex = 0;
    while (current != null) {
      if (currentInnerIndex == index) {
        T removed = current.value;
        if (currentInnerIndex == 0) {
          tail = current.next;
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
    Node<T> newNode = new Node<>(null, element);
    if (size == 0) {
      tail = newNode;
      size++;
      return true;
    }
    if (size == 1) {
      head = newNode;
      tail.next = head;
      size++;
      return true;
    }
    head.next = newNode;
    head = newNode;
    size++;
    return true;
  }

  @Override
  public void clear() {
    Node<T> current = tail;
    Node<T> next;
    while (current != null) {
      next = current.next;
      current.next = null;
      current.value = null;
      current = next;
    }
    tail = null;
    size = 0;
  }

  @Override
  public boolean contains(T element) {
    Node<T> current = tail;
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
    if (index == 0) {
      tail = new Node<>(tail, element);
      size++;
      return true;
    }
    Node<T> current = tail;
    Node<T> previous = null;
    int currentInnerIndex = 0;
    while (current != null) {
      if (currentInnerIndex == index) {
        previous.next = new Node<>(current, element);
        size++;
        return true;
      }
      previous = current;
      current = current.next;
      currentInnerIndex++;
    }
    return false;

  }

  @Override
  public int indexOf(T element) {
    Node<T> current = tail;
    int index = 0;
    while (current != null) {
      if (current.value.equals(element)) {
        return index;
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
    StringBuilder sb = new StringBuilder("MyLinkedList2{ ");
    sb.append(tail.value);
    Node<T> current = tail.next;
    while (current != null) {
      sb.append(", ").append(current.value);
      current = current.next;
    }
    sb.append(" }");
    return sb.toString();
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
  }
}
