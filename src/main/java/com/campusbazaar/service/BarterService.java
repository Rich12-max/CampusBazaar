package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.exception.UnauthorizedOperationException;
import com.campusbazaar.exception.ValidationException;
import com.campusbazaar.model.BarterListing;
import com.campusbazaar.model.BarterProposal;
import com.campusbazaar.model.Listing;
import com.campusbazaar.model.Product;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.enums.ItemCondition;
import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.ProposalStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing peer-to-peer item barter postings and swap proposals.
 */
public class BarterService {
    private final DataStore dataStore = DataStore.getInstance();

    public BarterListing createBarterListing(Product product, Student seller, String preferredExchangeItem,
                                            double cashAdjustment, boolean isNegotiable) throws ValidationException {
        if (preferredExchangeItem == null || preferredExchangeItem.trim().isEmpty()) {
            throw new ValidationException("Preferred exchange item cannot be empty.");
        }
        int listingId = dataStore.nextListingId();
        BarterListing listing = new BarterListing(listingId, product, seller, ListingStatus.AVAILABLE,
                preferredExchangeItem.trim(), cashAdjustment, isNegotiable);
        dataStore.addListing(listing);
        return listing;
    }

    public BarterProposal proposeSwap(int listingId, Student proposer, String offeredItemTitle,
                                      ItemCondition condition, String description, double cashAdjustment)
            throws ValidationException, UnauthorizedOperationException {
        Listing listing = dataStore.getListingById(listingId);
        if (listing == null || !(listing instanceof BarterListing)) {
            throw new ValidationException("Target listing is not available for barter/swap.");
        }
        if (listing.getSeller().getUserId() == proposer.getUserId()) {
            throw new UnauthorizedOperationException("You cannot submit a swap proposal to your own listing.");
        }
        if (offeredItemTitle == null || offeredItemTitle.trim().isEmpty()) {
            throw new ValidationException("Offered item title cannot be empty.");
        }

        int proposalId = dataStore.nextProposalId();
        BarterProposal proposal = new BarterProposal(proposalId, listingId, proposer.getUserId(),
                proposer.getFullName(), proposer.getStudentIdNumber(), offeredItemTitle.trim(),
                condition, description != null ? description.trim() : "", cashAdjustment, ProposalStatus.PENDING);
        dataStore.addBarterProposal(proposal);
        return proposal;
    }

    public void acceptProposal(int proposalId, int sellerId) throws UnauthorizedOperationException {
        BarterProposal proposal = dataStore.getBarterProposalById(proposalId);
        if (proposal == null) return;

        Listing listing = dataStore.getListingById(proposal.getListingId());
        if (listing == null || listing.getSeller().getUserId() != sellerId) {
            throw new UnauthorizedOperationException("Only the listing poster can accept this barter proposal.");
        }

        proposal.setStatus(ProposalStatus.ACCEPTED);
        listing.setStatus(ListingStatus.RESERVED);
    }

    public void rejectProposal(int proposalId, int sellerId, String reason) throws UnauthorizedOperationException {
        BarterProposal proposal = dataStore.getBarterProposalById(proposalId);
        if (proposal == null) return;

        Listing listing = dataStore.getListingById(proposal.getListingId());
        if (listing == null || listing.getSeller().getUserId() != sellerId) {
            throw new UnauthorizedOperationException("Only the listing poster can reject this barter proposal.");
        }

        proposal.setStatus(ProposalStatus.REJECTED);
        proposal.setSellerNotes(reason != null ? reason : "Declined by poster.");
    }

    public List<BarterProposal> getProposalsForListing(int listingId) {
        return dataStore.getBarterProposals().stream()
                .filter(p -> p.getListingId() == listingId)
                .collect(Collectors.toList());
    }

    public List<BarterProposal> getProposalsByStudent(int studentId) {
        return dataStore.getBarterProposals().stream()
                .filter(p -> p.getProposerId() == studentId)
                .collect(Collectors.toList());
    }
}

