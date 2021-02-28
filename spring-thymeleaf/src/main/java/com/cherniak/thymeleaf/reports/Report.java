package com.cherniak.thymeleaf.reports;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;


/**
 * Entity of REPORT table
 *
 * @author Artem Demyansky
 */
@Data
@Entity
@Table(name = "REPORT")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private Instant createDate;
    @Column(name = "SEND_DATE")
    private Instant sendDate;
    @Column(name = "COMPLETE_DATE")
    private Instant completeDate;
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    private String number;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    @Column(name = "ERROR_DESCRIPTION")
    private String errorDescription;
    @Column(name = "PVU_MSG_ID_IN")
    private String pvumInId;
    @Column(name = "PVU_MSG_ID_OUT")
    private String pvumOutId;

//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @OneToMany(
//        fetch = FetchType.LAZY,
//        cascade = CascadeType.REMOVE,
//        mappedBy = "report"
//    )
//    private List<Accident> accident = new ArrayList<>();

    public boolean isDone() {
        return ReportStatus.ERROR.equals(reportStatus) || ReportStatus.COMPLETED.equals(reportStatus);
    }
}
