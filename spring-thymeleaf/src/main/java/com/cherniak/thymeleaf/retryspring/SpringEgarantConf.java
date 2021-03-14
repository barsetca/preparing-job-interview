package com.cherniak.thymeleaf.retryspring;

import com.cherniak.thymeleaf.resilience.B2bConf;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SpringEgarantConf {
  private HttpClientConfiguration http;
  private AuthCredentionals authCredentionals;
  private B2bConf.Retries retries;

  @Data
  public static class HttpClientConfiguration {
    private String baseUrl;
    private Timeouts timeouts;


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
    private Duration delay = Duration.ofSeconds(10);
    private int tries = 1;
  }
}
