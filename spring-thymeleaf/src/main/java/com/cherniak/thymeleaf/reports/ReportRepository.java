package com.cherniak.thymeleaf.reports;

import java.time.Instant;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * REPORT repository
 *
 * @author Artem Demyansky
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    /**
     * Find report by type, number and status , loaded early
     *
     * @param reportType   type
     * @param number       number
     * @param reportStatus status
     * @return Optional<Report>
     */
    Optional<Report> findOneByReportTypeAndNumberAndReportStatusIsNotOrderByCreateDateDesc(ReportType reportType,
                                                                                         String number,
                                                                                         ReportStatus reportStatus);

    /**
     * Find bunch of reports and lock it
     *
     * @param status report status
     * @param p      number of rows
     * @return list of reports
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "-2")})
    Page<Report> findAllByReportStatus(ReportStatus status, Pageable p);


    Page<Report> findAllByReportStatus_(ReportStatus status, Pageable p);

    /**
     * Delete reports by id
     *
     * @param ids ids
     */
    @Modifying
    @Query("delete Report r where r.createDate < :createDate")
    void deleteBefore(@Param("createDate") Instant time);

}
