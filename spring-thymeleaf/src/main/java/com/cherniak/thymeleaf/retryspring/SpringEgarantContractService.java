package com.cherniak.thymeleaf.retryspring;

import static io.vavr.API.Tuple;

import com.cherniak.thymeleaf.alexander.AccidentReportServiceFinish;
import com.cherniak.thymeleaf.alexander.AccidentReportServiceRetry;
import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.resilience.RetryUtils;
import io.github.resilience4j.retry.Retry;
import io.vavr.API;
import io.vavr.CheckedFunction0;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@Slf4j
public class SpringEgarantContractService {

  @Autowired
  private AccidentReportServiceFinish reportService;

  @Autowired
  private FileService fileService;

  @Autowired
  private AccidentReportServiceRetry serviceRetry;

  @Setter(onMethod_ = {@Autowired(required = false), @Qualifier("retryEgarant")})
  private RetryTemplate retryTemplateEgarant;

  @Setter(onMethod_ = {@Autowired(required = false), @Qualifier("retryDb")})
  private RetryTemplate retryTemplateDb;

  @Setter(onMethod_ = {@Autowired(required = false), @Qualifier("retryB2b")})
  private RetryTemplate retryTemplateB2b;

  @Setter(onMethod_ = {@Autowired(required = false), @Qualifier("osagoRetry")})
  private Retry egarantRetry = RetryUtils.noop("osago");

  @Setter(onMethod_ = {@Autowired(required = false), @Qualifier("databaseRetry")})
  private Retry dbRetry = RetryUtils.noop("db");

  //@Setter(onMethod_ = {@Autowired(required = false), @Qualifier("b2bRetry")})
  private Retry b2bRetry = RetryUtils.noop("b2b");

  @Autowired(required = false)
  public void setB2bRetry(@Qualifier("b2bRetry") Retry b2bRetry) {
    this.b2bRetry = RetryUtils.noop("b2b");
  }

  @Setter(onMethod_ = {@Value("${contract-search-period:1h}")})
  private Duration searchInterval = Duration.ofHours(1);

  public String getIdEgarant() {
    String id = retryTemplateEgarant.execute(context -> {
      return serviceRetry.saveXlsFileEgarantRetry();
    });
    return id;
  }

  public String getIdDb() {
    String id = retryTemplateDb.execute(context -> {
      return serviceRetry.saveXlsFileDbRetry();
    });

//    String id = dbRetry.executeSupplier(
//        () -> {
//          return serviceRetry.saveXlsFileDbRetry();
//        }
//    );
    return id;
  }

  public String getIdB2b() {
    String id = retryTemplateB2b.execute(context -> {
      return serviceRetry.saveXlsFileB2bRetry();
    });
    return id;
  }

  public Map<Integer, Try<File>> getMapSpring() throws Throwable {
    List<Integer> ids = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<File> files = new ArrayList<>(
        Arrays.asList(new File(1, "One"), new File(2, "Two"), new File(2, "Three")));

    Map<Integer, Try<File>> getMap;
    try {
      getMap = ids.stream()
          .map(id -> Tuple(id,
              //  retryTemplateEgarant.execute(retryContext -> {
              //  return
              API.Try(
                  new CheckedFunction0<File>() {
                    @Override
                    public File apply() throws Throwable {
                      if (id == 3) {
//                  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                  /*
                  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                  1 -> Success(File(id=1, name=One, createDate=null, fileStatus=null))
                  2 -> Success(File(id=2, name=Two, createDate=null, fileStatus=null))
                  3 -> Failure(org.springframework.web.client.HttpServerErrorException: 500 INTERNAL_SERVER_ERROR)
                   */

                        throw new HttpServerErrorException(
                            HttpStatus.SERVICE_UNAVAILABLE); // вкл повтор

                        // throw new ResourceAccessException(String.format("0 record(s) in file or invalid format of id: %d", id)); // вкл повтор
                      }
                      return files.get(id - 1);
                    }
                  })
              //;
              // })
          ))
          .collect(Collectors.toMap(new Function<Tuple2<Integer, Try<File>>, Integer>() {
            @Override
            public Integer apply(Tuple2<Integer, Try<File>> stringTryTuple2) {
              return stringTryTuple2._1();
            }
          }, new Function<Tuple2<Integer, Try<File>>, Try<File>>() {
            @Override
            public Try<File> apply(Tuple2<Integer, Try<File>> integerTryTuple2) {
              return integerTryTuple2._2();
            }
          }));
      getMap.forEach((k, v) -> System.out.println(k + " -> " + v));
    } catch (Throwable e) {
      throw new AssertionError(e);
    }
    return getMap;
  }


  public Map<Integer, Try<File>> getMap() {
    List<Integer> ids = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<File> files = new ArrayList<>(
        Arrays.asList(new File(1, "One"), new File(2, "Two"), new File(2, "Three")));

    Map<Integer, Try<File>> getMap = ids.stream()
        .map(id -> Tuple(id, egarantRetry.executeTrySupplier(() -> API.Try(
            new CheckedFunction0<File>() {
              @Override
              public File apply() throws Throwable {
                if (id == 3) {
//                  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                  /*
                  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                  1 -> Success(File(id=1, name=One, createDate=null, fileStatus=null))
                  2 -> Success(File(id=2, name=Two, createDate=null, fileStatus=null))
                  3 -> Failure(org.springframework.web.client.HttpServerErrorException: 500 INTERNAL_SERVER_ERROR)
                   */

                  throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE); // вкл повтор

                  // throw new ResourceAccessException(String.format("0 record(s) in file or invalid format of id: %d", id)); // вкл повтор
                }
                return files.get(id - 1);
              }
            }))))
        .collect(Collectors.toMap(new Function<Tuple2<Integer, Try<File>>, Integer>() {
          @Override
          public Integer apply(Tuple2<Integer, Try<File>> stringTryTuple2) {
            return stringTryTuple2._1();
          }
        }, new Function<Tuple2<Integer, Try<File>>, Try<File>>() {
          @Override
          public Try<File> apply(Tuple2<Integer, Try<File>> integerTryTuple2) {
            return integerTryTuple2._2();
          }
        }));
    getMap.forEach((k, v) -> System.out.println(k + " -> " + v));

    return getMap;

  }


}
