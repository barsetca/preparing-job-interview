package com.cherniak.thymeleaf.resilience;

//import com.cherniak.thymeleaf.exception.ProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
//import org.springframework.web.server.ServerErrorException;


@Configuration
public class EgarantConfiguration {

  @Autowired
  EgarantConf conf;

  @Bean
  @ConfigurationProperties("eosago")
  B2bConf eosagoConf() {
    return new B2bConf();
  }

//  @Autowired
//  RestTemplateBuilder builder;

//  @Bean
//  MessageFormatter restTemplateHttpMessageFormatter() {
//    return new HttpMessageFormatter(List.of());
//  }

  @Bean
  @ConfigurationProperties("egarant")
  EgarantConf egarantConf() {
    return new EgarantConf();
  }

  @Bean
  Retry osagoRetry() {
    return Retry.of(
      "osago",
      RetryConfig.custom()
        .intervalFunction(new IntervalFunction() {
          @Override
          public Long apply(Integer attempt) {
            System.out.println("tries = " + attempt + " intervalFunction getInitialDelay() = " + conf.getRetries().getInitialDelay().toSeconds());
            System.out.println(attempt + " intervalFunction getInitialDelay()= " + conf.getRetries().getTransitionTime().toSeconds());
            System.out.println(attempt + " intervalFunction getInitialDelay()= " + conf.getRetries().getLongDelay().toSeconds());
            if (attempt * conf.getRetries().getInitialDelay().toMillis() < conf.getRetries()
                .getTransitionTime().toMillis()) {
              return conf.getRetries().getInitialDelay().toMillis();
            } else {
              return conf.getRetries().getLongDelay().toMillis();
            }
          }
        })
        .maxAttempts(conf.getRetries().getTries())
        .retryOnException(e -> {
          if (e instanceof HttpServerErrorException) {
            return ((HttpServerErrorException) e).getRawStatusCode() == 503;
          } else if (e instanceof ResourceAccessException) {
            return true;
          } else {
            return false;
          }
        })
        .build()
    );
  }

  @Bean
  Retry diasoftDatabaseRetry(
      @Value("${diasoft-db.retries.delay:10s}") Duration delay,
      @Value("${diasoft-db.retries.times:1}") int times
  ) {
    return Retry.of(
        "dbDiasoft",
        RetryConfig.custom()
            .maxAttempts(times + 1)
            .waitDuration(delay)
            .retryExceptions(TransientDataAccessException.class)
            .build()
    );
  }

  @Bean
  Retry b2bRetry(B2bConf conf) {
    return Retry.of(
        "b2b",
        RetryConfig.custom()
            .waitDuration(conf.getRetries().getInitialDelay())
            .maxAttempts(conf.getRetries().getTries())
            .retryExceptions(ProcessingException.class)
            .retryOnException(e -> {
              if (e instanceof IllegalArgumentException) {
                return e.getMessage().endsWith(".xlsx");
              }
              return false;
            })
//            .retryOnException(e -> {
//              if (e instanceof ServerErrorException) {
//
//                return ((ServerErrorException) e).getResponse().getStatus() == 503;
//              }
//              return false;
//            })
            .build()
    );
  }

  @Bean
  Retry databaseRetry(
      @Value("${inner-db.retries.delay:10s}") Duration delay,
      @Value("${inner-db.retries.times:1}") int times
  ) {
    System.out.println("@Value inner-db.retries.delay:10s = " + delay);
    System.out.println("inner-db.retries.times:1 = " + times);
    return Retry.of(
        "db",
        RetryConfig.custom()
            .maxAttempts(times + 1) //1
            .waitDuration(delay)
            .retryExceptions(TransientDataAccessException.class)
            .build()
    );
  }

//  @Bean
//  RestTemplate rsaTemplate(HttpClient httpClient) {
//    var restTemplate = builder.build();
//    for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
//      if (converter instanceof AbstractJackson2HttpMessageConverter) {
//        ObjectMapper mapper = ((AbstractJackson2HttpMessageConverter) converter).getObjectMapper();
//        mapper.registerModule(new JsonNullableModule());
//      }
//    }
//    restTemplate.getMessageConverters().add(new FileHttpMessageConverter());
//    // This allows us to read the response more than once - Necessary for debugging.
//    restTemplate.setRequestFactory(
//      new BufferingClientHttpRequestFactory(
//        new HttpComponentsClientHttpRequestFactory(httpClient)
//      )
//    );
//    return restTemplate;
//  }

//  @Bean(destroyMethod = "close")
//  @SneakyThrows
//  CloseableHttpClient httpClient() {
//    return HttpClientBuilder.create()
//      .setDefaultRequestConfig(
//        RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()
//      )
//      .setSSLContext(
//        SSLContexts.custom()
//          .loadTrustMaterial(
//            new TrustSelfSignedStrategy()
//          )
//          .build()
//      )
//      .build();
//  }



//  @Bean
//  ApiClient egarantApiClient(RestTemplate rsaTemplate) {
//    var client = new ApiClient(rsaTemplate);
//    client.setBasePath(conf.getHttp().getBaseUrl());
//    return client;
//  }
//
//  @Bean
//  EgarantClient egarantClient(
//    EgarantApi egarantApi,
//    ApiClient apiClient,
//    DocumentsApi documentsApi,
//    EgarantAuthenticator egarantAuthenticator
//  ) {
//    return new EgarantClient(
//      egarantApi,
//      apiClient,
//      documentsApi,
//      egarantAuthenticator
//    );
//  }

//  @Bean
//  EgarantAuthenticator egarantAuthenticator(DefaultApi defaultApi) {
//    return new EgarantAuthenticator(
//      conf.getAuthCredentionals(),
//      defaultApi
//    );
//  }
//
//  @Bean
//  DefaultApi defaultApi(ApiClient apiClient) {
//    return new DefaultApi(apiClient);
//  }
//
//  @Bean
//  EgarantApi egarantApi(ApiClient apiClient) {
//    return new EgarantApi(apiClient);
//  }
//
//
//  @Bean
//  DocumentsApi documentsApi(ApiClient apiClient) {
//    return new DocumentsApi(apiClient);
//  }
//
//  private static class FileHttpMessageConverter extends AbstractHttpMessageConverter<File> {
//    @Override
//    protected boolean supports(Class<?> clazz) {
//      return File.class == clazz;
//    }
//
//    @Override
//    public List<MediaType> getSupportedMediaTypes() {
//      return List.of(MediaType.ALL);
//    }
//
//    @Override
//    protected File readInternal(Class<? extends File> clazz, HttpInputMessage inputMessage)
//      throws IOException, HttpMessageNotReadableException {
//      var tempFile = File.createTempFile("http", "file");
//      try (var is = inputMessage.getBody(); var os = Files.newOutputStream(tempFile.toPath())) {
//        is.transferTo(os);
//      }
//      return tempFile;
//    }
//
//    @Override
//    protected void writeInternal(File file, HttpOutputMessage outputMessage)
//      throws IOException, HttpMessageNotWritableException {
//      throw new HttpMessageNotWritableException("Not supported");
//    }
//  }
}
