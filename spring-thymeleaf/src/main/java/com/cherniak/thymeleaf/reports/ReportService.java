package com.cherniak.thymeleaf.reports;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service for ReportRepository
 *
 * @author Artem Demyansky
 */
@Slf4j
@Service
@AllArgsConstructor
public class ReportService {
    private ReportRepository reportRepository;

    /**
     * Find report by id
     *
     * @param id id
     * @return report
     */
    public Report findReportById(Integer id) {
        if (id == null) {
            return null;
        }
        return reportRepository.findById(id).orElse(null);
    }

    /**
     * Find report by type, number and status , loaded early.
     * If nothing found, creates new
     *
     * @param type  type
     * @param value number
     * @return report
     */
    public Report findSameReport(String type, String value) {
        return reportRepository
            .findOneByReportTypeAndNumberAndReportStatusIsNotOrderByCreateDateDesc(
                mapType(type),
                value,
                ReportStatus.ERROR
            )
            .orElseGet(() -> {
                Report report = new Report();
                report.setReportType(mapType(type));
                report.setReportStatus(ReportStatus.INWORK);
                report.setNumber(value);
                reportRepository.saveAndFlush(report);
                return report;
            });
    }

    /**
     * map report type
     *
     * @param type
     * @return type
     */
    private ReportType mapType(String type) {

        switch (Objects.requireNonNull(type).trim().toUpperCase()) {
            case "VIN":
                return ReportType.Vin;
            case "ГРЗ":
                return ReportType.RegNum;
            case "НК":
                return ReportType.BodyNum;
            case "ИСС":
                return ReportType.Iss;
            default:
                throw new NullPointerException("Unknown report type " + type);
        }
    }

    /**
     * Complete report
     *
     * @param r            report
     * @param completeDate complete date
     * @param pvumOutId    id
     */
    public void completeReport(Report r, Instant completeDate, long pvumOutId) {
        r.setReportStatus(ReportStatus.COMPLETED);
        r.setCompleteDate(completeDate);
        r.setPvumOutId(String.valueOf(pvumOutId));
    }

    /**
     * Timed out report
     *
     * @param r report
     */
    public void timedOutReport(Report r) {
        r.setReportStatus(ReportStatus.ERROR);
        r.setErrorDescription("Время ожидания отчета ИС ПВУ истекло");
    }

    /**
     * Find bunch of INWORK reports and lock it
     *
     * @param maxRows number of rows
     * @return list of reports
     */
    public List<Report> findReadyForPvuSendingReports(int maxRows) {
        return reportRepository.findAllByReportStatus(ReportStatus.INWORK, PageRequest.of(0, maxRows))
            .toList();
    }

    /**
     * Find bunch of SENDED reports and lock it
     *
     * @param maxRows number of rows
     * @return list of reports
     */
    public List<Report> findReadyFoProcessingReports(int maxRows) {
        return reportRepository.findAllByReportStatus_(ReportStatus.SENDED, PageRequest.of(0, maxRows))
            .toList();
    }

//    /**
//     * Delete reports by id
//     *
//     * @param ids ids
//     */
    public void deleteBefore(Instant time) {
        reportRepository.deleteBefore(time);
    }
}
