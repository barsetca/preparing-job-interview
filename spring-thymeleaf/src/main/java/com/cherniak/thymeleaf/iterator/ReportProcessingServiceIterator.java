package com.cherniak.thymeleaf.iterator;

import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.files.FileStatus;
import com.cherniak.thymeleaf.reports.Report;
import com.cherniak.thymeleaf.reports.ReportService;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Slf4j
public class ReportProcessingServiceIterator {

  private final ReportService reportService;
  private final FileService fileService;

  @Async
  @Transactional
  public Future<Void> processFileAsync(Map<String, String> reportFile, File file) {
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
      //int size = file.getReports().size();
      //System.out.println("size " + size);
      fileService.save(file);
      log.info("File processed id={}", file.getId());
    } catch (Exception e) {
      log.warn("Failed to process file", e);
      file.setFileStatus(FileStatus.ERROR);
      fileService.save(file);
    }
    return null;
  }

  //@Async
  @Transactional
  public Future<Void> processFileAsyncIterator(String key, String value, File file) {
    try {
      log.debug("Processing {}:{}", key, value);
      Report r = reportService.findSameReport(value, key);
      file.getReports().add(r);
      file.setFileStatus(FileStatus.INWORK);
      fileService.save(file);
      log.info("File processed id={}", file.getId());
    } catch (NullPointerException e) {
      log.error("Can't save report with type {} and value {} : {}", key, value, e.getMessage());
    } catch (Exception e) {
      log.warn("Failed to process file", e);
      file.setFileStatus(FileStatus.ERROR);
      fileService.save(file);
    }
    return null;
  }

  //@Async
  @Transactional
  public void processFileAsyncConsumer(String key, String value, File file) {
    try {
//      TimeUnit.MICROSECONDS.sleep(1);
      log.debug("Processing {}:{}", key, value);
      Report r = reportService.findSameReport(value, key);
      file.getReports().add(r);
//      file.setFileStatus(FileStatus.INWORK);
//      fileService.save(file);
//      log.info("File processed id={}", file.getId());
    } catch (NullPointerException e) {
      log.error("Can't save report with type {} and value {} : {}", value, key, e.getMessage());
    } catch (Exception e) {
      log.warn("Failed to process file", e);
      file.setFileStatus(FileStatus.ERROR);
      fileService.save(file);
    }
   // return null;
  }

  //@Async
  @Transactional
  public Future<Void> processFileAsyncConsumer2(String key, String value, File file) {
    try {
      //TimeUnit.SECONDS.sleep(10);
      log.debug("Processing {}:{}", key, value);
      Report r = reportService.findSameReport(value, key);
      file.getReports().add(r);
      file.setFileStatus(FileStatus.INWORK);
      fileService.save(file);
      log.info("File processed id={}", file.getId());

    } catch (NullPointerException e) {
      log.error("Can't save report with type {} and value {} : {}", key, value, e.getMessage());
    } catch (Exception e) {
      log.warn("Failed to process file", e);
      file.setFileStatus(FileStatus.ERROR);
      fileService.save(file);
    }
    return null;
  }
}
