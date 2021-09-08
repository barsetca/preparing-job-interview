package com.cherniak.thymeleaf.retryspring;

//import com.cherniak.thymeleaf.exception.ProcessingException;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.support.RetryTemplate;
//import org.springframework.web.server.ServerErrorException;


@Configuration
//@EnableRetry
public class SpringRetryConfiguration {


  @Bean
  @ConfigurationProperties("osago")
  SpringB2bConf osagoConf() {
    return new SpringB2bConf();
  }

  //
  @Bean
  @ConfigurationProperties("garant")
  SpringEgarantConf springEgarantConf() {
    return new SpringEgarantConf();
  }

  @Bean("retryEgarant")
  public RetryTemplate retryTemplateEgarant(SpringEgarantConf conf) {
    var backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(conf.getRetries().getInitialDelay().toMillis());
    var listener = new RetryListenerSupport() {
      @Override
      public <T, E extends Throwable> void onError(RetryContext context,
          RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("Интервал = " + backOffPolicy.getBackOffPeriod() + " ms");
        System.out.println("№ текущей попытки = " + context.getRetryCount());
        if (context.getRetryCount() * conf.getRetries().getInitialDelay().toMillis() >= conf
            .getRetries().getTransitionTime().toMillis()) {
          backOffPolicy.setBackOffPeriod(conf.getRetries().getLongDelay().toMillis());
        }
        super.onError(context, callback, throwable);
      }
    };

//    RetryListener retryListener = new RetryListener() {
//      @Override
//      public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
//        return true;
//      }
//      @Override
//      public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
//      }
//      @Override
//      public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
//        System.out.println("Интервал = " + backOffPolicy.getBackOffPeriod() + " ms");
//        System.out.println("№ тулущей попытки = " + retry.incrementAndGet());
//        if (retry.get() >= 10) {
//          backOffPolicy.setBackOffPeriod(conf.getRetries().getLongDelay().toMillis());
//        }
//      }
//    };
//    RetryTemplate template = new RetryTemplate();
//    template.registerListener(listener);
//    template.setRetryPolicy(new EgarantRetryPolicy(conf.getRetries().getTries()));
//    template.setBackOffPolicy(backOffPolicy);

    return RetryTemplate.builder()
        .withListener(listener)
        .customBackoff(backOffPolicy)
        .customPolicy(new EgarantRetryPolicy(conf.getRetries().getTries()))
        .build();
  }

  @Bean("retryDb")
  public RetryTemplate retryTemplateDb(
      @Value("${inner-db.retries.delay:10s}") Duration delay,
      @Value("${inner-db.retries.tries:2}") int tries
  ) {

//    Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
//    retryableExceptions.put(TransientDataAccessException.class, true);
//    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(times + 1, retryableExceptions);
//    FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
//    backOffPolicy.setBackOffPeriod(delay.toMillis());
//    RetryTemplate template = new RetryTemplate();
//    template.setRetryPolicy(retryPolicy);
//    template.setBackOffPolicy(backOffPolicy);
    return RetryTemplate.builder()
        .retryOn(TransientDataAccessException.class)
        .maxAttempts(tries)
        .fixedBackoff(delay.toMillis())
        .build();
  }

  @Bean("retryB2b")
  public RetryTemplate retryTemplateB2b(SpringB2bConf osagoConf) {

//    Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
//    retryableExceptions.put(IllegalArgumentException.class, true);
//    //retryPolicy.setMaxAttempts(5);
//    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(conf.getRetries().getTries(), retryableExceptions);
//    FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
//    backOffPolicy.setBackOffPeriod(conf.getRetries().getInitialDelay().toMillis());
//
//    RetryTemplate template = new RetryTemplate();
//    template.setRetryPolicy(new B2bRetryPolicy(conf.getRetries().getTries()));
//    template.setBackOffPolicy(backOffPolicy);

    return RetryTemplate.builder()
        .fixedBackoff(osagoConf.getRetries().getInitialDelay().toMillis())
        .customPolicy(new B2bRetryPolicy(osagoConf.getRetries().getTries()))
        .build();
  }
}
