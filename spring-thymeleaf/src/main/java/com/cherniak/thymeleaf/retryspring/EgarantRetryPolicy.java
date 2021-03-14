package com.cherniak.thymeleaf.retryspring;

import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

public class EgarantRetryPolicy extends ExceptionClassifierRetryPolicy {

  public EgarantRetryPolicy(int maxAttempts) {
    final NeverRetryPolicy doNotRetry = new NeverRetryPolicy();
    final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(maxAttempts);

    this.setExceptionClassifier(throwable -> {
      if (throwable instanceof HttpServerErrorException) {
        if (((HttpServerErrorException) throwable).getRawStatusCode() == 503) {
          return simpleRetryPolicy;
        }
      }
      if (throwable instanceof ResourceAccessException) {
        return simpleRetryPolicy;
      }
      return doNotRetry;
    });
  }
}
