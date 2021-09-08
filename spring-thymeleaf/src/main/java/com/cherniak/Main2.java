package com.cherniak;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2 {

  public static final Pattern MODEL_CODE_PATTERN = Pattern.compile("(.*)(\\d{6})$");
  public static final String REMOVE_TO_REDIRECT_PATTERN = "\\?code=\\d*$";
  public static final Pattern SIZE_DOUBLE_PATTERN = Pattern.compile("^(\\d+,\\d+).*");
  public static final String SIZE_PREFIX = "Размер ";

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

    List<String> list = new ArrayList<>();
    list.add("Размер 135х200 См, Золотой");//
    list.add("Размер 155х200 См, Бирюза/Кофе С Молоком");//
    list.add("Размер 150х200 См, Нежно-Розовый");//
    list.add("Размер 44/46, Антрацит");//

    list.add("Размер 150х200 См");//
    list.add("Размер 150х200 См, Красная Фуксия");//
    list.add("Размер 48, Бежевый");//
    list.add("Размер 48, Черно-Белый");

    list.add("Размер 44/48, Серо-Коричневый");//
    list.add("Размер 50х68 См");//
    list.add("Размер 17");//

    list.add("Размер L, Черный");//
    list.add("Размер Xl, Черный");//
    list.add("Размер Xxl, Черный");//
    list.add("Размер 48, Леопард Натуральный");//
    list.add("Размер 40,5, Черный");//
    list.add("Размер 40-42, Черный");//
    list.add("Размер 40, Черный");//
    list.add("Размер 40,5");//

    list.add("Размер 7,50");
    list.add("Размер 77777,5");//
    list.add("Размер 77,500001");//
    list.add("23;36");
    list.add("21;34");

    list.forEach(s -> {

      if (s.startsWith(SIZE_PREFIX)) {
        String withoutPrefix = s.replace(SIZE_PREFIX, "");
        Matcher matcher = SIZE_DOUBLE_PATTERN.matcher(withoutPrefix);
        if (matcher.matches()) {
          String code = matcher.group(1);
          System.out.println("/////////////////////// MATCHER");
          System.out.println("RESULT = " + withoutPrefix + " code =" + code);
          System.out.println("code length = " + code.length());

        } else {
          System.out.println("############################################# SPLIT");
          String[] variationName = withoutPrefix.trim().split(",");
          System.out.println("RESULT = " + withoutPrefix + " size =" + variationName[0]);
          System.out.println("code length = " + variationName[0].length());
        }
    }
      else {
        String[] split = s.split(";");
        System.out.println("________________________________-------------------------------- NOT ");
        System.out.println(" RESULT = " + split[split.length-1]);
      }
    });
  }
}
