package com.preparing.job.interview.lesson2algorithms;

public class Main {

  public static void main(String[] args) {

    System.out.println("///////////////////MyList<Integer> myList = new MyArrayList<>()//////////");
    MyList<Integer> myList = new MyArrayList<>();
    System.out.println(myList.getCapacity() + " " + myList.size());// 8 0
    myList.add(1);
    myList.add(2);
    myList.add(3);
    myList.add(4);
    myList.add(5);
    myList.add(6);
    myList.add(7);
    myList.add(8);
    System.out.println(myList.add(9)); //true
    System.out.println(myList.getCapacity()); // 17 (8 -> 17)
    System.out.println(myList.contains(3)); // true
    System.out.println(myList.size()); // 9
    System.out.println(myList); //MyArrayList{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }
    myList.insert(5, 1000);
    System.out.println(myList.size()); //10
    System.out.println(myList.get(5)); // 1000
    System.out.println(myList.indexOf(1000)); //5
    System.out.println(myList); // MyArrayList{ 1, 2, 3, 4, 5, 1000, 6, 7, 8, 9 }
    System.out.println(myList.remove(5)); // 1000 - deleted element
    System.out.println(myList.size()); //9
    System.out.println(myList); // MyArrayList{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }
    myList.clear();
    System.out.println(myList.contains(3)); // false
    System.out.println(myList.size()); // 0

    System.out.println();
    System.out.println("///////////////////MyList<Integer> linked = new MyLinkedList<>()/////////");
    MyList<Integer> linked = new MyLinkedList<>();
    linked.add(1);
    linked.add(2);
    linked.add(3);
    linked.add(4);
    linked.add(5);
    linked.add(6);
    linked.add(7);
    linked.add(8);
    System.out.println(linked.add(9)); //true
    System.out.println(linked.getCapacity()); // 0
    System.out.println(linked.contains(3)); // true
    System.out.println(linked.size()); // 9
    System.out.println(linked); //MyArrayList{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }
    linked.insert(5, 1000);
    System.out.println(linked.size()); //10
    System.out.println(linked.get(5)); // 1000
    System.out.println(linked.indexOf(1000)); //5
    System.out.println(linked); // MyArrayList{ 1, 2, 3, 4, 5, 1000, 6, 7, 8, 9 }
    System.out.println(linked.remove(5)); // 1000 - deleted element
    System.out.println(linked.size()); //9
    System.out.println(linked); // MyArrayList{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }
    linked.clear();
    System.out.println(linked.contains(3)); // false
    System.out.println(linked.size()); // 0

    System.out.println();
    System.out.println("////////////////MyList<Integer> linked2 = new MyLinkedList2<>()/////////");
    MyLinkedList2<Integer> linked2 = new MyLinkedList2<>();
    linked2.add(1);
    linked2.add(2);
    linked2.add(3);
    linked2.add(4);
    linked2.add(5);
    linked2.add(6);
    linked2.add(7);
    linked2.add(8);
    System.out.println(linked2.add(9)); //true
    System.out.println(linked2.getCapacity()); // 0
    System.out.println(linked2.contains(3)); // true
    System.out.println(linked2.size()); // 9
    System.out.println(linked2); //MyArrayList2{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }
    linked2.insert(5, 1000);
    System.out.println(linked2.size()); //10
    System.out.println(linked2.get(5)); // 1000
    System.out.println(linked2.indexOf(1000)); //5
    System.out.println(linked2); // MyArrayList2{ 1, 2, 3, 4, 5, 1000, 6, 7, 8, 9 }
    System.out.println(linked2.remove(5)); // 1000 - deleted element
    System.out.println(linked2.size()); //9
    System.out.println(linked2); // MyArrayList2{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }
    linked2.clear();
    System.out.println(linked2.contains(3)); // false
    System.out.println(linked2.size()); // 0
  }
}
