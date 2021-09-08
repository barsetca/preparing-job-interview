package com.cherniak;

import static org.springframework.util.StringUtils.hasText;

public class Main3 {

  public static void main(String[] args) {
    String url = "fdhdskljf/jashfcjsdjkfc/asjkfhdjh?q=%3Atoprated";
    String param = "";
    if (hasText(url)) {
      String[] split = url.split("\\?");
      param = split[split.length-1];
      for (String s : split) {
        System.out.println(s);
      }

    }

    System.out.println(param);
  }

}
