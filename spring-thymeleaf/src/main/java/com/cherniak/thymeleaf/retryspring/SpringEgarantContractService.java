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
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpringEgarantContractService {

  @Autowired
  private AccidentReportServiceFinish reportService;

  @Autowired
  private FileService fileService;

  @Autowired
  private AccidentReportServiceRetry serviceRetry;

  private RetryTemplate egarantRetry;

  private RetryTemplate dbRetry;

  private RetryTemplate b2bRetry;

  @Autowired(required = false)
  public void setEgarantRetry(@Qualifier("retryEgarant") RetryTemplate egarantRetry) { //osagoRetry
    this.egarantRetry = RetryUtils.noopSpring("osago", egarantRetry);
  }

  @Autowired(required = false)
  public void setDbRetry(@Qualifier("retryDb") RetryTemplate dbRetry) { // databaseRetry
    this.dbRetry = RetryUtils.noopSpring("db", dbRetry);
  }

  @Autowired(required = false)
  public void setB2bRetry(@Qualifier("retryB2b") RetryTemplate b2bRetry) { // b2bRetry
    this.b2bRetry = RetryUtils.noopSpring("b2b", b2bRetry);
  }

  @Setter(onMethod_ = {@Value("${contract-search-period:1h}")})
  private Duration searchInterval = Duration.ofHours(1);

  public String getIdEgarant() {
    try {
      CompletableFuture.runAsync(() -> egarantRetry.execute(context -> {
        serviceRetry.saveXlsFileEgarantRetry();
        return null;
      })).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    String id = egarantRetry.execute(context -> {
      return serviceRetry.saveXlsFileEgarantRetry();
    });
    return id;
  }

  public String getIdDb() {
    //String id = dbRetry.execute(context -> serviceRetry.saveXlsFileDbRetry());

    String id = serviceRetry.saveXlsFileDbRetry();

//    String id = dbRetry.executeSupplier(
//        () -> {
//          return serviceRetry.saveXlsFileDbRetry();
//        }
//    );
    return id;
  }

  public String getIdB2b() {

//    CompletableFuture<Void> future
//        = CompletableFuture.supplyAsync(() -> {
//          b2bRetry.execute(context -> {
//            serviceRetry.saveXlsFileB2bRetry();
//            return null;
//          });
//          return null;
//        });
    try{
    //CompletableFuture<Void> futureRun =
        CompletableFuture.runAsync(() -> b2bRetry.execute(context -> {
          serviceRetry.saveXlsFileB2bRetry();
          return null;
        })).get();


//    ExecutorService executor = Executors.newSingleThreadExecutor();
//    Future<Void> future = executor.submit(new Callable<Void>() {
//      @Override
//      public Void call() throws Exception {
//        b2bRetry.execute((RetryCallback<Void, RuntimeException>) context -> {
//          serviceRetry.saveXlsFileB2bRetry();
//          return null;
//        });
//        return null;
//      }
//    });


      //futureRun.get();
      //future.get();
      return "future.get()";
    } catch (InterruptedException | ExecutionException e) {
     throw new RuntimeException(e);
    }

//    final String[] id = {null};

//    try {
//      String[] id = new String[1];
//      System.out.println("Основной поток - " + Thread.currentThread().getName());
//      new Thread(() -> id[0] = b2bRetry.execute(context -> serviceRetry.saveXlsFileB2bRetry()))
//          .start();
//      return "Gjkexbkb id" + id[0];
//    }finally {
//      System.out.println("Мы здесь /////////////////////////////////////////////////////////////////////////");
//    }

//    b2bRetry.execute(context -> {
//      new Thread(new Runnable() {
//        @Override
//        public void run() {
//          serviceRetry.saveXlsFileB2bRetry();
//        }
//      }).start();
//      return null;
//    });






//      id = b2bRetry.execute(context -> {
//        try {
//        Future<String> future = executorService.submit(new Callable<String>() {
//          @Override
//          public String call() throws Exception {
//            return serviceRetry.saveXlsFileB2bRetry();
//          }
//        });
//        return future.get();
//        } catch (Throwable e) {
//          throw new RuntimeException(e);
//        }
//      });
//    b2bRetry.execute(context -> {
//
//      new Thread(new Runnable() {
//        @Override
//        public void run() {
//          id[0] = serviceRetry.saveXlsFileB2bRetry();
//        }
//      }).start();
//      return null;
//    });
//
//      return id[0];
   // return id[0];

  }

  public Map<Integer, Try<File>> getMapSpring() throws Throwable {
    List<Integer> ids = new ArrayList<>(Arrays.asList(1, 2, 3));
    ListFileService listFileService = new ListFileService();
    Map<Integer, Try<File>> getMap;
   // try {
      getMap = ids.stream()
          .map(id -> {
            return Tuple(id,
                API.Try(
                    new CheckedFunction0<File>() {
                      @Override
                      public File apply() throws Throwable {

                        return egarantRetry.execute(retryContext -> {
                          return listFileService.getByIndex(id - 1);
                        });
                      }
                    })
            );
          })
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
//    } catch (Throwable e) {
//      throw new AssertionError(e);
//    }
    return getMap;
  }

  public Map<Integer, Try<File>> getMapSpring2() throws Throwable {
    List<Integer> ids = new ArrayList<>(Arrays.asList(1, 2, 3));
    ListFileService listFileService = new ListFileService();
//    List<File> files = new ArrayList<>(
//        Arrays.asList(new File(1, "One"), new File(2, "Two"), new File(2, "Three")));

    Map<Integer, Try<File>> getMap;
       getMap = ids.stream()
            .map(id -> Tuple(id,
                //  retryTemplateEgarant.execute(retryContext -> {
                //  return
                API.Try(
                    new CheckedFunction0<File>() {
                      @Override
                      public File apply() throws Throwable {
                        return listFileService.getByIndex(id - 1);
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
      return getMap;
  }

//  public Map<Integer, Try<File>> getMap() {
//    List<Integer> ids = new ArrayList<>(Arrays.asList(1, 2, 3));
//    List<File> files = new ArrayList<>(
//        Arrays.asList(new File(1, "One"), new File(2, "Two"), new File(2, "Three")));
//
//    Map<Integer, Try<File>> getMap = ids.stream()
//        .map(id -> Tuple(id, egarantRetry.executeTrySupplier(() -> API.Try(
//            new CheckedFunction0<File>() {
//              @Override
//              public File apply() throws Throwable {
//                if (id == 3) {
////                  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
//                  /*
//                  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
//                  1 -> Success(File(id=1, name=One, createDate=null, fileStatus=null))
//                  2 -> Success(File(id=2, name=Two, createDate=null, fileStatus=null))
//                  3 -> Failure(org.springframework.web.client.HttpServerErrorException: 500 INTERNAL_SERVER_ERROR)
//                   */
//
//                  throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE); // вкл повтор
//
//                  // throw new ResourceAccessException(String.format("0 record(s) in file or invalid format of id: %d", id)); // вкл повтор
//                }
//                return files.get(id - 1);
//              }
//            }))))
//        .collect(Collectors.toMap(new Function<Tuple2<Integer, Try<File>>, Integer>() {
//          @Override
//          public Integer apply(Tuple2<Integer, Try<File>> stringTryTuple2) {
//            return stringTryTuple2._1();
//          }
//        }, new Function<Tuple2<Integer, Try<File>>, Try<File>>() {
//          @Override
//          public Try<File> apply(Tuple2<Integer, Try<File>> integerTryTuple2) {
//            return integerTryTuple2._2();
//          }
//        }));
//    getMap.forEach((k, v) -> System.out.println(k + " -> " + v));
//
//    return getMap;

//  }


}
