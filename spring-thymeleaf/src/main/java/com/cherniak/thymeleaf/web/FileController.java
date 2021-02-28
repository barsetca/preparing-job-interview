package com.cherniak.thymeleaf.web;

import com.cherniak.thymeleaf.service.AccidentReportService;
import com.cherniak.thymeleaf.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

  private final AccidentReportService accidentReportService;

  @GetMapping
  public String parseFile(){
    return accidentReportService.saveXlsFile();
  }

}
