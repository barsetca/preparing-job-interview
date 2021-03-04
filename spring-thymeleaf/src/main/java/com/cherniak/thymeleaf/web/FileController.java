package com.cherniak.thymeleaf.web;

import com.cherniak.thymeleaf.alexander.AccidentReportServiceFinish;
import com.cherniak.thymeleaf.iterator.AccidentReportServiceIterator;
import com.cherniak.thymeleaf.service.AccidentReportService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

  private final AccidentReportService accidentReportService;
  private final AccidentReportServiceIterator accidentReportServiceIterator;
  private final AccidentReportServiceFinish accidentReportServiceFinish;

  @GetMapping
  public String parseFile() {
    return accidentReportService.saveXlsFile();
  }

  @GetMapping("/iterator")
  public String parseIterator() {
       return accidentReportServiceIterator.saveXlsFile();
  }

  @GetMapping("/without")
//  @SneakyThrows
  public String parseIteratorWithout() {
    //Future<String> id = accidentReportServiceIterator.saveXlsFileWithoutMap();
    return accidentReportServiceIterator.saveXlsFileWithoutMap();
    //return accidentReportServiceIterator.saveXlsFileWithoutMap().get();
//    String id = null;
//    try {
//      id = accidentReportServiceIterator.saveXlsFileWithoutMap().get();
//    } catch (InterruptedException | ExecutionException e) {
//      throw new IllegalArgumentException("Does not data in file");
//    }
//    return id;
  }
  @GetMapping("/consumer")
  public String parseConsumer() {
    return accidentReportServiceIterator.saveXlsFileWithBiConsumer();
  }

  @GetMapping("/finish")
  public String parseConsumer2() {
    return accidentReportServiceFinish.saveXlsFileAlexanderThread();
  }

}
