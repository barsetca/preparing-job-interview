package com.cherniak.thymeleaf.alexander;

import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.reports.ReportService;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;


/**
 * Service provides business logic for download or upload request
 *
 * @author Artem Demyansky
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccidentReportServiceRetry {

  private final FileService fileService;
  private final ExcelServiceFinish excelService;
  private final ReportProcessingServiceFinish reportProcessingService;
  private final ReportService reportService;

  @Transactional
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFileFinish() {

//    java.io.File multipartFile = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");

//    java.io.File multipartFile = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\text.txt");

    java.io.File multipartFile = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input1line.xlsx");
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
//        throw new IllegalArgumentException(
//            String.format("0 record(s) in file or invalid format of file: %s", file.getName()));

//        throw new NullPointerException(
//            String.format("0 record(s) in file or invalid format of file: %s", file.getName()));
        throw new ConcurrencyFailureException(String.format("0 record(s) in file or invalid format of file: %s", file.getName())); //databaseRetry extends TransientDataAccessException
        //throw new ProcessingException(String.format("0 record(s) in file or invalid format of file: %s", file.getName()));
        //throw new ResourceAccessException(String.format("0 record(s) in file or invalid format of file: %s", file.getName())); //sago = egarantRetry
//        throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE); // osago = egarantRetry infinity
        // throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR); //osago = egarantRetry at once
      }


      log.info("Main finished {}", Thread.currentThread().getName());
      return String.valueOf(file.getId());

    } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
