package com.cherniak.thymeleaf.alexander;

import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.files.FileStatus;
import com.cherniak.thymeleaf.reports.ReportService;
import com.sun.jdi.InternalException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


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

  /**
   * Parse and save rows from file to database
   *
   * @param
   * @return file id
   */
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
        } catch (IOException e) {
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


}
