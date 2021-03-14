package com.cherniak.thymeleaf.retryspring;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SpringB2bConf {
  private HttpClientConfiguration http;
  private AuthCredentionals authCredentionals;
  private Retries retries;

  @Data
  public static class HttpClientConfiguration {
    private String baseUrl;
    private Timeouts timeouts = new Timeouts();

    @Data
    public static class Timeouts {
      private Duration read = Duration.ofSeconds(30);
      private Duration connection = Duration.ofSeconds(1);
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AuthCredentionals {
    private String username;
    private String password;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Retries {
    private Duration initialDelay = Duration.ofSeconds(5); // conf = 5
    private Duration longDelay = Duration.ofSeconds(30); // conf = 30
    private Duration transitionTime = Duration.ofMinutes(5); // conf = 5
    private int tries = Integer.MAX_VALUE;
  }
}
