package com.cherniak.thymeleaf;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Main {

  public static final Pattern MODEL_CODE_PATTERN = Pattern.compile("(.*)(\\d{6})$");
  public static final String REMOVE_TO_REDIRECT_PATTERN = "\\?code=\\d*$";

  public static void main(String[] args) {

//    String value = "Appia Due Жакет";
//    String[] str = value.toLowerCase(Locale.ROOT).trim().split("");
//    System.out.println(Arrays.toString(str));
//    System.out.println(String.join("" , str));

//    List<String> list = new ArrayList<>();
//    list.add("product-code-model-123456");
//    list.add("123456");
//    list.add("product-code-model");
//    list.add("product-code-model-123456125");
//    list.add("prod4uct-code12-model3-123456");
//    list.add("prodФuct-codeП2-model3-123456");
//    list.add("prodФuct-codeП2-model3-123");
//    list.forEach(s -> {
//      Matcher matcher = MODEL_CODE_PATTERN.matcher(s);
//      if (matcher.matches()){
//        String name = matcher.group(1);
//        String code = matcher.group(2);
//        System.out.println("RESULT = " + s +" name = " + name + " code = " + code) ;
//      } else {
//        System.out.println(s + " RESULT = " + matcher.matches());
//      }
//redirect:/p/monari-kurtka-151060?code=151060003
//    });

//    List<String> list = new ArrayList<>();
//    list.add("redirect:/p/monari-kurtka-151060?code=151060003");
//    list.add("product-code-model-123456?code:");
//    list.add("123456?code:1");
//    list.add("product-code-model?code:45652332");
//    list.add("product-code-model-123456125?code:?code:");
//    list.add("prod4uct-code12-model3-123456?code:63463?code:");
//    list.add("prodФuct-codeП2-model3-123456?code:545623?code:54598989");
//    list.add("prodФuct-codeП2-model3-123");
//    list.forEach(s -> {
      //Matcher matcher = MODEL_CODE_PATTERN.matcher(s);
      //if (matcher.matches()){
//      String result = s.replaceAll(REMOVE_TO_REDIRECT_PATTERN, "");
//
//      System.out.println("RESULT:  sorce = " + s + " result = " + result);
      //} else {
      //  System.out.println(s + " RESULT = " + matcher.matches());
      // }

//    });

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //Проверка что передается ссылка
    List<String> list = new ArrayList<>();
    fillList(list);
    //list.forEach(s -> System.out.println(s));
    List<String> list2 = new ArrayList<>();
    //fillList2(list2);
    //union(list2 == null ? new ArrayList<>() : list2, list).forEach(s -> System.out.println(s));

//    List<String> existUnboundProducts = list == null ? new ArrayList<>() : new ArrayList<>(list);
//    List<String> existUnboundCategories = list2 == null ? new ArrayList<>() : new ArrayList<>(list2);
//    System.out.println("list2");
//    existUnboundCategories.forEach(s -> System.out.println(s));
//    System.out.println("list");
//    existUnboundProducts.forEach(s -> System.out.println(s));
//    System.out.println("After remove list 2 from list");

    //remove(existUnboundProducts, existUnboundCategories);

//    existUnboundProducts.removeAll(existUnboundCategories);
//    System.out.println(existUnboundProducts);
//    existUnboundProducts.forEach(s -> System.out.println(s));

//    System.out.println("After remove list from list2");
//    remove(existUnboundCategories, existUnboundProducts);
//    existUnboundCategories.forEach(s -> System.out.println(s));
//    System.out.println(existUnboundCategories.isEmpty());
    String s = " ";
    System.out.println(validateString(s));
    System.out.println(!validateString(s.trim()));


  }

  private static boolean validateString(String string){
    return string!=null&&!string.isEmpty();}

  private static <T> void remove(List<T> list1, List<T> list2) {
    list1.removeAll(list2);
  }

  private static <T> List<T> union(List<T> list1, List<T> list2) {
    Set<T> set = new HashSet<T>();
    set.addAll(list1);
    set.addAll(list2);
    return new ArrayList<T>(set);
  }



  public static void fillList(List<String> list){
    for (int i = 0; i < 10; i++) {
      list.add("list " + i);
    }
  }

  public static void fillList2(List<String> list){
    for (int i = 0; i < 5; i++) {
      list.add("list " + i);
    }
  }
}
