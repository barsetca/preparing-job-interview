package com.cherniak.thymeleaf.web;

import com.cherniak.thymeleaf.iterator.AccidentReportServiceIterator;
import com.cherniak.thymeleaf.service.AccidentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

  private final AccidentReportService accidentReportService;
  private final AccidentReportServiceIterator accidentReportServiceIterator;

  @GetMapping
  public String parseFile() {
    return accidentReportService.saveXlsFile();
  }

  @GetMapping("/iterator")
  public String parseIterator() {
       return accidentReportServiceIterator.saveXlsFile();
  }

  @GetMapping("/without")
  public String parseIteratorWitout() {
    return accidentReportServiceIterator.saveXlsFileWithoutMap();
  }
}
