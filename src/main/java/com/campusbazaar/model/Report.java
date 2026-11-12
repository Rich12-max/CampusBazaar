package com.campusbazaar.model;

import com.campusbazaar.model.enums.ReportStatus;
import java.time.LocalDateTime;

public class Report {
    private int reportId;
    private int reporterId;
    private String reporterName;
    private int reportedUserId;
    private String reportedUserName;
    private int reportedListingId;
    private String reportedListingTitle;
    private String reason;
    private String description;
    private ReportStatus status;
    private String adminNotes;
    private LocalDateTime createdAt;

    public Report(int reportId, int reporterId, String reporterName, int reportedUserId, String reportedUserName,
                  int reportedListingId, String reportedListingTitle, String reason, String description,
                  ReportStatus status) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.reportedUserId = reportedUserId;
        this.reportedUserName = reportedUserName;
        this.reportedListingId = reportedListingId;
        this.reportedListingTitle = reportedListingTitle;
        this.reason = reason;
        this.description = description;
        this.status = status;
        this.adminNotes = "";
        this.createdAt = LocalDateTime.now();
    }

    public int getReportId() {
        return reportId;
    }

    public int getReporterId() {
        return reporterId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public String getReportedUserName() {
        return reportedUserName;
    }

    public int getReportedListingId() {
        return reportedListingId;
    }

    public String getReportedListingTitle() {
        return reportedListingTitle;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

