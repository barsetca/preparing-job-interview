package com.cherniak.thymeleaf.files;

import com.cherniak.thymeleaf.reports.ReportStatus;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Service for file repository
 *
 * @author Artem Demyansky
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @PersistenceContext
        //(unitName = "primary")
    private EntityManager entityManager;



    /**
     * Find file in repository by file id
     *
     * @param id id
     * @return Optional<File>
     */
    public Optional<File> find(Integer id) {
        return fileRepository.findById(id);
    }

    /**
     * Create file in repository
     *
     * @param name name
     * @return file
     */
    public File create(String name) {
        File file = new File();
        file.setFileStatus(FileStatus.RESERVED);
        file.setName(name);
        fileRepository.save(file);
        return file;
    }

    public File save(File file) {
        return fileRepository.save(file);
    }

    public long getNotProcessedReportsCount(Integer fileId) {
        var processedReportsCount = entityManager.createQuery("select (select count(r) from f.reports r where r.reportStatus = :reportSuccessStatus or r.reportStatus = :reportErrorStatus) from File f where f.id = :fileId", Long.class)
            .setParameter("fileId", fileId)
            .setParameter("reportSuccessStatus", ReportStatus.COMPLETED)
            .setParameter("reportErrorStatus", ReportStatus.ERROR)
            .getSingleResult();
        var totalReportsCount = fileRepository.findById(fileId).get().getReports().size();

        return totalReportsCount - processedReportsCount;
    }

    public int deleteOrphanFiles() {
        return entityManager.createQuery("delete from File f where size(f.reports) = 0").executeUpdate();
    }
}
