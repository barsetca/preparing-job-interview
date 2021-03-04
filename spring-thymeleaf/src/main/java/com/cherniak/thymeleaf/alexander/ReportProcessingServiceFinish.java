package com.cherniak.thymeleaf.alexander;

import com.cherniak.thymeleaf.files.File;
import com.cherniak.thymeleaf.files.FileService;
import com.cherniak.thymeleaf.files.FileStatus;
import com.cherniak.thymeleaf.reports.Report;
import com.cherniak.thymeleaf.reports.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Slf4j
public class ReportProcessingServiceFinish {
    private final ReportService reportService;
    private final FileService fileService;

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
}
