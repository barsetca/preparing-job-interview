package com.cherniak.thymeleaf.service;

import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.reports.ReportService;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
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
public class AccidentReportService {

  private final FileService fileService;
  private final ReportService reportService;
  //private final AccidentService accidentService;
  private final ExcelService excelService;
  private final ReportProcessingService reportProcessingService;
  //private final AccidentReportConfig config;

//  /**
//   * Parse and save rows from file to database
//   *
//   * @param multipartFile
//   * @return file id
//   */
  @Transactional
  //public String saveXlsFile(MultipartFile multipartFile) {
  public String saveXlsFile() {
    //log.info("Parsing file {}", multipartFile.getOriginalFilename());
    java.io.File fileTest = new java.io.File(
        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input.xlsx");
//    java.io.File fileTest = new java.io.File(
//        "C:\\newprojects\\preparing-job-interview\\spring-thymeleaf\\input0line.xlsx");
    try {
      Map<String, String> parsedFile;
      //try (var is = new BufferedInputStream(multipartFile.getInputStream())) {
      try (var is = new BufferedInputStream(new FileInputStream(fileTest))) {

        parsedFile = excelService.parseFile(is);
        parsedFile.forEach((k, v) -> System.out.println(k + " -> " + v));
      }
     // File file = fileService.create(multipartFile.getOriginalFilename());
      File file = fileService.create("OriginalFilename");
      reportProcessingService.processFileAsync(parsedFile, file);
      return String.valueOf(file.getId());
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
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
