package com.cherniak.thymeleaf.alexander;

import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.files.FileStatus;
import com.cherniak.thymeleaf.reports.Report;
import com.cherniak.thymeleaf.reports.ReportService;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Slf4j
public class ReportProcessingServiceFinish {

  private final ReportService reportService;
  private final FileService fileService;
  private final ExcelServiceFinish excelService;

/*
  @Transactional
  public void processFile(String key, String value, File file) {
    try {
      log.debug("Processing {}:{}", key, value);
      Report r = reportService.findSameReport(value, key);
      file.getReports().add(r);
    } catch (NullPointerException e) {
      log.error("Can't save report with type {} and value {} : {}", value, key, e.getMessage());
    } catch (Exception e) {
      log.warn("Failed to process file", e);
      file.setFileStatus(FileStatus.ERROR);
      fileService.save(file);
    }
  }


  @Transactional
  public void processFileThread(String key, String value, File file, List<Report> reports) {
    try {
      //   if (key == null && value == null) {
//            file.setFileStatus(FileStatus.INWORK);
//            fileService.save(file);
//            log.info("File processed id={}", file.getId());
      // } else {
      log.debug("Processing {}:{}", key, value);
      Report r = reportService.findSameReport(value, key);
      //reports.add(r);
      file.getReports().add(r);
      file.setFileStatus(FileStatus.INWORK);
      fileService.save(file);
      // }

    } catch (NullPointerException e) {
      log.error("Can't save report with type {} and value {} : {}", value, key, e.getMessage());
//        } catch (Exception e) {
//            log.warn("Failed to process file", e);
//            file.setFileStatus(FileStatus.ERROR);
//            fileService.save(file);
//        }
    }
  }

  @Async
  @Transactional
  public Future<Void> processFileAlexander(Map<String, String> reportFile, File file) {
    try {
      reportFile
          .forEach((key, value) -> {
            try {
              log.debug("Processing {}:{}", key, value);
              Report r = reportService.findSameReport(value, key);
              file.getReports().add(r);

            } catch (NullPointerException e) {
              log.error("Can't save report with type {} and value {} : {}", key, value,
                  e.getMessage());
            }
          });
      file.setFileStatus(FileStatus.INWORK);
      fileService.save(file);
      log.info("File processed id={}", file.getId());

    } catch (Exception e) {
      log.warn("Failed to process file", e);
      file.setFileStatus(FileStatus.ERROR);
      fileService.save(file);
    }
    return null;
  }

  */
  @Async
  @Transactional
  public Future<Void> parseProcess(
      InputStream is,
      File file,
      CompletableFuture<Boolean> isEmpty
  ) {
    //try (var is = new BufferedInputStream(new FileInputStream(multipartFile))) {
    try  {

      excelService.parseFileAlexanderFinish(
          is,
          (value, type) -> {
            try {
              Report r = reportService.findSameReport(type, value);
              file.getReports().add(r);
              // check catch (Exception e) block
//              if ("XW7BF4HK10S109118".equals(key)) {
//                throw new NullPointerException();
//              }
            } catch (NullPointerException e) {
              log.error("Can't save report with type {} and value {} : {}", type, value, e.getMessage());
            }
          },
          isEmpty
      );
      file.setFileStatus(FileStatus.INWORK);
      fileService.save(file);
      // check catch (Exception e) block
//      if ("input.xlsx".equals(file.getName())){
//        throw new IOException("My custom IOException");
//      }
      log.info("File processed id={}", file.getId());

    } catch (IllegalArgumentException e) {
      log.warn("Failed to save: 0 record(s) in file or invalid format of file: {}",
          file.getName(), e);
      isEmpty.complete(true);
    } catch (Exception e) {
      log.warn("Failed to save fileName = {}", file.getName(), e);
      file.setFileStatus(FileStatus.ERROR);
      fileService.save(file);
    }
    return null;
  }
}
