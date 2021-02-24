package com.preparing.job.interview.lesson6httpsocketclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MyHttpSocketClient {

  private static class MyResponse {

    public MyResponse(InputStream inputStream) {

      try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          System.out.println(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println("getAll");
    sendRequest("GET", 8189, "/app", "/students", "/", "application/json", "");
    System.out.println("getById");
    sendRequest("GET", 8189, "/app", "/students", "/1", "application/json", "");
    System.out.println("create @RequestParam");
    sendRequest("POST", 8189, "/app", "/students", "?id=5&name=Mark", "application/json", "");
    System.out.println("create @RequestBody");
    sendRequest("POST", 8189, "/app", "/students", "/body", "application/json", "{\"id\":100,\"name\":\"Sto\"}");
    System.out.println("getAll");
    sendRequest("GET", 8189, "/app", "/students", "/", "application/json", "");
  }

  private static void sendRequest(String method, int port, String rootPath, String controllerPath,
      String addToPath, String contentType, String body) throws Exception {
    try (Socket socket = new Socket("localhost", port)) {
      StringBuilder out = new StringBuilder();
      out.append(method).append(" ").append(rootPath).append(controllerPath).append(addToPath)
          .append(" HTTP/1.1").append("\r\n");
      out.append("Host: ").append("localhost:").append(port).append("\r\n");
      out.append("Accept: ").append(contentType).append(";charset=UTF-8").append("\r\n");
      out.append("Connection: ").append("close").append("\r\n");
      out.append("Content-Type: ").append(contentType).append(";charset=UTF-8").append("\r\n");
      out.append("Content-Length: ").append(body.length()).append("\r\n");
      out.append("\r\n");
      out.append(body).append("\r\n");
      socket.getOutputStream().write(out.toString().getBytes(StandardCharsets.UTF_8));
      socket.getOutputStream().flush();
      new MyResponse(socket.getInputStream());
    }
  }
}
