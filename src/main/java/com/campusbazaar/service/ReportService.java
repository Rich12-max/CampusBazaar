package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Listing;
import com.campusbazaar.model.Report;
import com.campusbazaar.model.User;
import com.campusbazaar.model.enums.ReportStatus;

import java.util.List;

public class ReportService {
    private final DataStore dataStore = DataStore.getInstance();

    public Report submitReport(User reporter, int reportedUserId, int reportedListingId, String reason, String details) {
        User reportedUser = reportedUserId > 0 ? dataStore.getUserById(reportedUserId) : null;
        Listing reportedListing = reportedListingId > 0 ? dataStore.getListingById(reportedListingId) : null;

        String userName = reportedUser != null ? reportedUser.getFullName() : "N/A";
        String listingTitle = reportedListing != null ? reportedListing.getProduct().getTitle() : "N/A";

        if (reportedUser != null) {
            // increment report count
            if (reportedUser instanceof com.campusbazaar.model.interfaces.Reportable) {
                ((com.campusbazaar.model.interfaces.Reportable) reportedUser).incrementReportCount();
            }
        }
        if (reportedListing != null) {
            reportedListing.incrementReportCount();
        }

        int repId = dataStore.nextReportId();
        Report rep = new Report(repId, reporter.getUserId(), reporter.getFullName(),
                reportedUserId, userName, reportedListingId, listingTitle, reason, details, ReportStatus.OPEN);
        dataStore.addReport(rep);
        return rep;
    }

    public List<Report> getAllReports() {
        return dataStore.getReports();
    }

    public void resolveReport(int reportId, String adminNotes) {
        for (Report r : dataStore.getReports()) {
            if (r.getReportId() == reportId) {
                r.setStatus(ReportStatus.RESOLVED);
                r.setAdminNotes(adminNotes);
                break;
            }
        }
    }
}

