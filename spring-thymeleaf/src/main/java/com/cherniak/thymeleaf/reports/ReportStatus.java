package com.cherniak.thymeleaf.reports;

/**
 * Status of report
 *
 * @author Artem Demyansky
 */
public enum ReportStatus {
    INWORK,
    SENDED,
    SUBMITED, //Deprecated since v 1.1.0
    COMPLETED,
    ERROR
}
