package com.cherniak.thymeleaf.files;

import com.cherniak.thymeleaf.reports.Report;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;


/**
 * Entity of File table
 *
 * @author Artem Demyansky
 */
@Entity
@Data
@Table(name = "FILE")
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private Instant createDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private FileStatus fileStatus;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    @JoinTable(
        name = "MTM_FILE2REPORT",
        joinColumns = @JoinColumn(name = "file_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "report_id", referencedColumnName = "ID")
    )
    @BatchSize(size = 64)
    private List<Report> reports = new ArrayList<>();

    public void addReport(Report report){
        reports.add(report);
    }

    public File(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
