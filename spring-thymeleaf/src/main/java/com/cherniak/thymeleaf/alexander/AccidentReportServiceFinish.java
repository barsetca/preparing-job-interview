package com.cherniak.thymeleaf.alexander;

import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.reports.ReportService;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class AccidentReportServiceFinish {

  private final FileService fileService;
  private final ExcelServiceFinish excelService;
  private final ReportProcessingServiceFinish reportProcessingService;
  private final ReportService reportService;

  /**
   * Parse and save rows from file to database
   *
   * @param
   * @return file id
   */

  /*
  @Transactional
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFile() {

    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input0line.xlsx");
    String fileName = fileTest.getName();
    log.info("Parsing file {}", fileName);
    try (var is = new BufferedInputStream(new FileInputStream(fileTest))) {
      Exchanger<Boolean> exchangerIsEmptyTable = new Exchanger<>();
      File file = fileService.create(fileName);
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.execute(() -> {
        try {
          excelService.parseFile(
              is,
              exchangerIsEmptyTable,
              (key, value) -> reportProcessingService.processFile(key, value, file));
          file.setFileStatus(FileStatus.INWORK);
          fileService.save(file);
          log.info("File processed id={}", file.getId());
        } catch (IOException | ParserConfigurationException e) {
          log.warn("Failed to process fileName = {}", fileName, e);
          file.setFileStatus(FileStatus.ERROR);
          fileService.save(file);
        }
      });
      executorService.shutdown();

      if (exchangerIsEmptyTable.exchange(null, 1, TimeUnit.MINUTES)) {
        throw new IllegalArgumentException("0 record(s) in file");
      }

      return String.valueOf(file.getId());
    } catch (IOException | InterruptedException | TimeoutException e) {
      throw new IllegalArgumentException(e);
    }
  }
  @Transactional
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFileAlexander() {

    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input0line.xlsx");
    String fileName = fileTest.getName();
    log.info("Parsing file {}", fileName);
    try (var is = new BufferedInputStream(new FileInputStream(fileTest))) {
      File file = fileService.create(fileName);
      var data = new HashMap<String, String>();
      excelService.parseFileAlexander(is,
          (key, value) -> {
            data.put(key, value);
          });
      if (data.isEmpty()) {
        throw new IllegalArgumentException("0 record(s) in file");
      }
      reportProcessingService.processFileAlexander(data, file);
      return String.valueOf(file.getId());
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    } catch (ParserConfigurationException e) {
      throw new IllegalArgumentException(e); //todo
    }
  }

  @Transactional
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFileAlexanderThread() {

    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input1line.xlsx");
    String fileName = fileTest.getName();
    log.info("Parsing file {}", fileName);
    try  {
      File file = fileService.create(fileName);
      var isEmpty = new CompletableFuture<Boolean>();
      List<Report> reports = file.getReports();
     // List<Report> reports = new ArrayList<>();

      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.execute(() -> {
        try(var is = new BufferedInputStream(new FileInputStream(fileTest))) {
          excelService.parseFileAlexanderThread(
              is,
              (key, value) -> {
                Report r = reportService.findSameReport(value, key);
                //reports.add(r);
                file.addReport(r);
                //file.getReports().add(r);


                //reportProcessingService.processFileThread(key, value, file, null);
              },
              isEmpty
          );
          //file.setReports(reports);
          file.setFileStatus(FileStatus.INWORK);
          fileService.save(file);
          log.info("File processed id={}", file.getId());
          //} catch (IOException | SAXException | OpenXML4JException | ParserConfigurationException e) {
        } catch (Exception e) {
          log.warn("Failed to process fileName = {}", fileName, e);
          file.setFileStatus(FileStatus.ERROR);
          fileService.save(file);
        }

      });
      executorService.shutdown();

//      new Thread(new Runnable() {
//        @Override
//        public void run() {
//          try {
//            excelService.parseFileAlexanderThread(
//                is,
//                (key, value) -> {
//              reportProcessingService.processFileThread(key, value, file);
//            },
//                isEmpty
//            );
//            file.setFileStatus(FileStatus.INWORK);
//            fileService.save(file);
//            log.info("File processed id={}", file.getId());
//          } catch (IOException e) {
//            e.printStackTrace();
//          }
//        }
//      }).start();

//      if (data.isEmpty()) {
//        throw new IllegalArgumentException("0 record(s) in file");
//      }
      //reportProcessingService.processFileAlexander(data, file);
      if (isEmpty.get(10, TimeUnit.SECONDS)) {
        throw new IllegalArgumentException("0 record(s) in file");
      }
      log.info("Main finished {}", Thread.currentThread().getName());
      return String.valueOf(file.getId());
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new IllegalArgumentException(e);
    }
  }

  */
  @Transactional
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFileFinish() {

    java.io.File multipartFile = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");

//    java.io.File multipartFile = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\text.txt");

//    java.io.File multipartFile = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input1line.xlsx");
    String fileName = multipartFile.getName();
    log.info("Parsing file {}", fileName);

    try (var is = new BufferedInputStream(new FileInputStream(multipartFile))) {
//            if (fileName.equals("input.xlsx")){
//        throw new IOException("My custom IOException");
//      }
      File file = fileService.create(fileName);
      var isEmpty = new CompletableFuture<Boolean>();
      reportProcessingService.parseProcess(is, file, isEmpty);

      if (isEmpty.get(10, TimeUnit.SECONDS)) {
        throw new IllegalArgumentException(
            String.format("0 record(s) in file or invalid format of file: %s", file.getName()));
      }


      log.info("Main finished {}", Thread.currentThread().getName());
      return String.valueOf(file.getId());

    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
}
