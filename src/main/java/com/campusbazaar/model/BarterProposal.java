package com.campusbazaar.model;

import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.model.enums.ProposalStatus;

import java.time.LocalDateTime;

/**
 * Encapsulates a student's offer to swap an item for a BarterListing.
 */
public class BarterProposal {
    private int proposalId;
    private int listingId;
    private int proposerId;
    private String proposerName;
    private String proposerRollNo;
    private String offeredItemTitle;
    private ItemCondition offeredItemCondition;
    private String offeredItemDescription;
    private double offeredCashAdjustment;
    private ProposalStatus status;
    private LocalDateTime createdAt;
    private String sellerNotes;

    public BarterProposal(int proposalId, int listingId, int proposerId, String proposerName, String proposerRollNo,
                          String offeredItemTitle, ItemCondition offeredItemCondition, String offeredItemDescription,
                          double offeredCashAdjustment, ProposalStatus status) {
        this.proposalId = proposalId;
        this.listingId = listingId;
        this.proposerId = proposerId;
        this.proposerName = proposerName;
        this.proposerRollNo = proposerRollNo;
        this.offeredItemTitle = offeredItemTitle;
        this.offeredItemCondition = offeredItemCondition;
        this.offeredItemDescription = offeredItemDescription;
        this.offeredCashAdjustment = offeredCashAdjustment;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.sellerNotes = "";
    }

    public int getProposalId() { return proposalId; }
    public void setProposalId(int proposalId) { this.proposalId = proposalId; }

    public int getListingId() { return listingId; }
    public void setListingId(int listingId) { this.listingId = listingId; }

    public int getProposerId() { return proposerId; }
    public void setProposerId(int proposerId) { this.proposerId = proposerId; }

    public String getProposerName() { return proposerName; }
    public void setProposerName(String proposerName) { this.proposerName = proposerName; }

    public String getProposerRollNo() { return proposerRollNo; }
    public void setProposerRollNo(String proposerRollNo) { this.proposerRollNo = proposerRollNo; }

    public String getOfferedItemTitle() { return offeredItemTitle; }
    public void setOfferedItemTitle(String offeredItemTitle) { this.offeredItemTitle = offeredItemTitle; }

    public ItemCondition getOfferedItemCondition() { return offeredItemCondition; }
    public void setOfferedItemCondition(ItemCondition offeredItemCondition) { this.offeredItemCondition = offeredItemCondition; }

    public String getOfferedItemDescription() { return offeredItemDescription; }
    public void setOfferedItemDescription(String offeredItemDescription) { this.offeredItemDescription = offeredItemDescription; }

    public double getOfferedCashAdjustment() { return offeredCashAdjustment; }
    public void setOfferedCashAdjustment(double offeredCashAdjustment) { this.offeredCashAdjustment = offeredCashAdjustment; }

    public ProposalStatus getStatus() { return status; }
    public void setStatus(ProposalStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public String getSellerNotes() { return sellerNotes; }
    public void setSellerNotes(String sellerNotes) { this.sellerNotes = sellerNotes; }
}

