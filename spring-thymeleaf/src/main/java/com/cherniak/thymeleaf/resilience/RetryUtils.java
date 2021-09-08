package com.cherniak.thymeleaf.resilience;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.springframework.retry.support.RetryTemplate;

@UtilityClass
public class RetryUtils {
  private final Map<String, Retry> cachedRetries = Collections.synchronizedMap(new HashMap<>());
  private final Map<String, RetryTemplate> cachedRetryTemplates = Collections.synchronizedMap(new HashMap<>());

  public Retry noop(String name) {
    return cachedRetries.computeIfAbsent(
      name,
        new Function<String, Retry>() {
          @Override
          public Retry apply(String n) {
            return Retry
                .of(n, RetryConfig.custom().maxAttempts(1).waitDuration(Duration.ZERO).build());
          }
        }
    );
  }

  public RetryTemplate noopSpring(String name, RetryTemplate retryTemplate) {
    cachedRetryTemplates.putIfAbsent(name, retryTemplate);
    return cachedRetryTemplates.get(name);
  }

}
