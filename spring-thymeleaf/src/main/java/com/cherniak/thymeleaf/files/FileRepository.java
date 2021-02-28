package com.cherniak.thymeleaf.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * File repository
 *
 * @author Artem Demyansky
 */
@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
}
