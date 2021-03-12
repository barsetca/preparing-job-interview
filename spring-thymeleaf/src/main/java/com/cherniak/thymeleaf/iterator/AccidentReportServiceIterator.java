package com.cherniak.thymeleaf.iterator;

import ch.qos.logback.core.util.TimeUtil;
import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.files.FileStatus;
import com.cherniak.thymeleaf.reports.ReportService;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service provides business logic for download or upload request
 *
 * @author Artem Demyansky
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccidentReportServiceIterator {

  private final FileService fileService;
  private final ReportService reportService;
  //private final AccidentService accidentService;
  private final ExcelServiceIterator excelServiceIterator;
  private final ReportProcessingServiceIterator reportProcessingServiceIterator;
  //private final AccidentReportConfig config;

//  /**
//   * Parse and save rows from file to database
//   *
//   * @param multipartFile
//   * @return file id
//   */
  @Transactional
  //@Async
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFile() {
    //log.info("Parsing file {}", multipartFile.getOriginalFilename());
    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");
    //fileWithoutDataTest
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input0line.xlsx");

//    try {

      //try (var is = new BufferedInputStream(multipartFile.getInputStream())) {
      try (var is = new BufferedInputStream(new FileInputStream(fileTest))) {

        File file = fileService.create("OriginalFilename");
        excelServiceIterator.parseFile(is, file);
        //parsedFile.forEach((k, v) -> System.out.println(k + " -> " + v));
//      }
     // File file = fileService.create(multipartFile.getOriginalFilename());

      //reportProcessingServiceIterator.processFileAsync(parsedFile, file);
      return String.valueOf(file.getId());
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Transactional
  //@Async
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFileWithoutMap() {
  //public Future<String> saveXlsFileWithoutMap() {
    //log.info("Parsing file {}", multipartFile.getOriginalFilename());
    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");

    //fileWithoutDataTest
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input0line.xlsx");

//    try {

    //try (var is = new BufferedInputStream(multipartFile.getInputStream())) {
    try (var is = new BufferedInputStream(new FileInputStream(fileTest))) {

      File file = fileService.create("OriginalFilename");
      excelServiceIterator.parseExcelWithoutMapping(is, file);
      //parsedFile.forEach((k, v) -> System.out.println(k + " -> " + v));
//      }
      // File file = fileService.create(multipartFile.getOriginalFilename());

      //reportProcessingServiceIterator.processFileAsync(parsedFile, file);
      return String.valueOf(file.getId());
      //return new AsyncResult<>(String.valueOf(file.getId()));
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
/*
excelServiceIterator.parseExcelWithoutMapping(is, file, new BiConsumer<String, String>() {
        @Override
        public void accept(String key, String value) {
          reportProcessingServiceIterator.processFileAsyncIterator(key, value, file);
        }
      });
 */
/*
  @Transactional
  //@Async
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFileWithBiConsumer() {
    //public Future<String> saveXlsFileWithoutMap() {
    //log.info("Parsing file {}", multipartFile.getOriginalFilename());
    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");

  //  fileWithoutDataTest
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input1line.xlsx");

 //try {

    //try (var is = new BufferedInputStream(multipartFile.getInputStream())) {
   // try (var is = new BufferedInputStream(new FileInputStream(fileTest))) {
    long startTime = System.currentTimeMillis();
      File file = fileService.create("OriginalFilename");

    AtomicReference<Throwable> errorReference = new AtomicReference<>();
//      Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
//      @Override
//      public void uncaughtException(Thread th, Throwable ex) {
//        System.out.println("Uncaught exception: " + ex);
//        throw new IllegalArgumentException("0 record(s) in file");
//      }
//    };
     // /*
      Thread t = new Thread(new Runnable() {
        //@SneakyThrows
        @Override
        public void run() {

            //Thread.sleep(10000);

          try (var is = new BufferedInputStream(new FileInputStream(fileTest))){
            excelServiceIterator.parseExcelWithoBiConsumer(is, file,
                (key, value) -> reportProcessingServiceIterator.processFileAsyncConsumer(key, value, file));
          } catch (IOException e) {
            e.printStackTrace();
          }
          file.setFileStatus(FileStatus.INWORK);
          fileService.save(file);
          log.info("File processed id={}", file.getId());
        }
      });
    t.setUncaughtExceptionHandler((th, ex) -> {errorReference.set(ex);});
    t.start();

//    ExecutorService executorService = Executors.newSingleThreadExecutor();
//
//    Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
//      @Override
//      public Boolean call() throws Exception {
//        Throwable newThreadError = null;
//        try {
//          TimeUnit.MILLISECONDS.sleep(500);
//          newThreadError = errorReference.get();
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//        return newThreadError!= null;
//      }
//    });
//
//    try {
//      if (future.get()){
//        throw new IllegalArgumentException("0 record(s) in file");
//      }
//    } catch (InterruptedException | ExecutionException e) {
//      e.printStackTrace();
//    }

//    try {
//      TimeUnit.MILLISECONDS.sleep(500);
//      Throwable newThreadError = errorReference.get();
//      if (newThreadError!= null) {
//        System.out.println(newThreadError.getMessage());
//        throw new IllegalArgumentException("0 record(s) in file");
//      }
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
    // */

//    try (var is = new BufferedInputStream(new FileInputStream(fileTest))){
//      excelServiceIterator.parseExcelWithoBiConsumer(is, file,
//          (key, value) -> reportProcessingServiceIterator.processFileAsyncConsumer(key, value, file));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    file.setFileStatus(FileStatus.INWORK);
//    fileService.save(file);
//    log.info("File processed id={}", file.getId());

  //parsedFile.forEach((k, v) -> System.out.println(k + " -> " + v));
//      }
  // File file = fileService.create(multipartFile.getOriginalFilename());

  //reportProcessingServiceIterator.processFileAsync(parsedFile, file);
//  long endTime = System.currentTimeMillis();
//    log.info("File processed id={} time={}ms", file.getId(), (endTime - startTime));
//      return String.valueOf(file.getId());
  //return new AsyncResult<>(String.valueOf(file.getId()));
//    } catch (IOException e) {
//      throw new IllegalArgumentException(e);
//    }
//}


  @Transactional
  //@Async
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFileWithBiConsumer() {
    //log.info("Parsing file {}", multipartFile.getOriginalFilename());
    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");
//      fileWithoutDataTest
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input1line.xlsx");
    try (var is = new BufferedInputStream(new FileInputStream(fileTest))){

    long startTime = System.currentTimeMillis();
    Exchanger<Boolean> exchangerIsEmptyTable = new Exchanger<>();
    File file = fileService.create("OriginalFilename");
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(() -> {
      try {
        excelServiceIterator.parseExcelWithoBiConsumer(
            is,
            exchangerIsEmptyTable,
            (key, value) -> reportProcessingServiceIterator.processFileAsyncConsumer(key, value, file));
        file.setFileStatus(FileStatus.INWORK);
        fileService.save(file);
        log.info("File processed id={}", file.getId());

      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    executorService.shutdown();

      if (exchangerIsEmptyTable.exchange(null, 1, TimeUnit.MINUTES)) {
        throw new IllegalArgumentException("0 record(s) in file");
      }

    long endTime = System.currentTimeMillis();
    log.info("File processed id={} time={}ms", file.getId(), (endTime - startTime));
    return String.valueOf(file.getId());
    } catch (IOException | InterruptedException | TimeoutException e) {
      throw new IllegalArgumentException(e);
    }

  }

//  @Transactional
//  public String saveXlsFileWithBiConsumer2() {
//
//        java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");
//
//    //  fileWithoutDataTest
////    java.io.File fileTest = new java.io.File(
////        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input1line.xlsx");
//
//    long startTime = System.currentTimeMillis();
//    File file = fileService.create("OriginalFilename");
//
//    try (var is = new BufferedInputStream(new FileInputStream(fileTest))){
//      excelServiceIterator.parseExcelWithoBiConsumer2(is, file,
//          (key, value) -> reportProcessingServiceIterator.processFileAsyncConsumer2(key, value, file));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
////    file.setFileStatus(FileStatus.INWORK);
////    fileService.save(file);
////    log.info("File processed id={}", file.getId());
//
//    //parsedFile.forEach((k, v) -> System.out.println(k + " -> " + v));
////      }
//    // File file = fileService.create(multipartFile.getOriginalFilename());
//
//    //reportProcessingServiceIterator.processFileAsync(parsedFile, file);
//    long endTime = System.currentTimeMillis();
//    log.info("File processed id={} time={}ms", file.getId(), (endTime - startTime));
//    return String.valueOf(file.getId());
//  }

  @Transactional
  //@Async
   public String saveXlsFileWithBiConsumer3() {

    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");

    //  fileWithoutDataTest
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input1line.xlsx");

    long startTime = System.currentTimeMillis();
    File file = fileService.create("OriginalFilename");

    AtomicReference<Throwable> errorReference = new AtomicReference<>();
    // /*
    Thread t = new Thread(new Runnable() {
      //@SneakyThrows
      @Override
      public void run() {

        try (var is = new BufferedInputStream(new FileInputStream(fileTest))){
          excelServiceIterator.parseExcelWithoBiConsumer(is, new Exchanger<Boolean>(),
              (key, value) -> reportProcessingServiceIterator.processFileAsyncConsumer(key, value, file));
        } catch (IOException e) {
          e.printStackTrace();
        }
          file.setFileStatus(FileStatus.INWORK);
          //fileService.save(file);
          log.info("File processed id={}", file.getId());
      }
    });
    t.setUncaughtExceptionHandler((th, ex) -> {errorReference.set(ex);});
    t.start();

    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

//    try (var is = new BufferedInputStream(new FileInputStream(fileTest))){
//      excelServiceIterator.parseExcelWithoBiConsumer(is, file,
//          (key, value) -> reportProcessingServiceIterator.processFileAsyncConsumer(key, value, file));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    file.setFileStatus(FileStatus.INWORK);
//    fileService.save(file);
//    log.info("File processed id={}", file.getId());

    long endTime = System.currentTimeMillis();
    log.info("File processed id={} time={}ms", file.getId(), (endTime - startTime));
    return String.valueOf(file.getId());

  }

  /**
   * Generate file from database rows
   *
   * @param id file id
   * @return file
   */
//  @Transactional
//  public InputStream getXlsFile(String id) {
//    try {
//      List<Accident> allAccidents = new LinkedList<>();
//
//      File file = fileService.find(Integer.valueOf(id))
//          .orElseThrow(() -> new IllegalArgumentException("No file was found with id " + id));
//
//      if (file.getFileStatus().equals(FileStatus.RESERVED)) {
//        throw new IllegalStateException("File is not loaded yet");
//      }
//      String fileName = file.getName();
//
//      if (Instant.now()
//          .isAfter(file.getCreateDate().plusMillis(config.getDb().getCriticalTime().toMillis()))) {
//        log.info("File was created before critical time elapsed");
//        file.getReports().stream().filter(Predicate.not(Report::isDone))
//            .peek(r -> log.warn("Found timed out report id={} for file {}", r.getId(), fileName))
//            .forEach(reportService::timedOutReport);
//      } else {
//        var notProcessedReportsCount = fileService.getNotProcessedReportsCount(Integer.valueOf(id));
//        if (notProcessedReportsCount > 0) {
//          throw new InternalException(
//              String.format("%s record(s) are not processed yet", notProcessedReportsCount));
//        }
//      }
//
//      List<Accident> completeAccidents = accidentService.findAccidents(
//          file.getReports().stream()
//              .filter(r -> ReportStatus.COMPLETED.equals(r.getReportStatus()))
//              .map(Report::getId)
//              .collect(Collectors.toList())
//      );
//
//      List<Accident> errorAccidents = file.getReports()
//          .stream()
//          .filter(r -> ReportStatus.ERROR.equals(r.getReportStatus()))
//          .map(r -> {
//            Accident errorAccident = new Accident();
//            errorAccident.setReport(r);
//
//            if (ReportType.Vin.equals(r.getReportType())) {
//              errorAccident.setVin(r.getNumber());
//            } else if (ReportType.BodyNum.equals(r.getReportType())) {
//              errorAccident.setBodyNum(r.getNumber());
//            } else if (ReportType.Iss.equals(r.getReportType())) {
//              errorAccident.setIss(r.getNumber());
//            } else if (ReportType.RegNum.equals(r.getReportType())) {
//              errorAccident.setRegNum(r.getNumber());
//            }
//            return errorAccident;
//          }).collect(Collectors.toList());
//
//      allAccidents.addAll(completeAccidents);
//      allAccidents.addAll(errorAccidents);
//
//      file.setFileStatus(FileStatus.DOWNLOADED);
//
//      return excelService.generateFile(allAccidents, file.getCreateDate());
//
//    } catch (IOException e) {
//      log.error("Can't generate file", e);
//      throw new FileGenerationException(e);
//    }
//  }
}
