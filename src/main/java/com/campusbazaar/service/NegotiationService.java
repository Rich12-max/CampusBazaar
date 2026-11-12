package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.exception.UnauthorizedOperationException;
import com.campusbazaar.exception.ValidationException;
import com.campusbazaar.model.Listing;
import com.campusbazaar.model.PriceNegotiation;
import com.campusbazaar.model.SaleListing;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.enums.NegotiationStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing student price negotiations and counter-offers (up to 3 rounds).
 */
public class NegotiationService {
    private final DataStore dataStore = DataStore.getInstance();

    public PriceNegotiation startNegotiation(int listingId, Student buyer, double initialOffer, String buyerNote)
            throws ValidationException, UnauthorizedOperationException {
        Listing listing = dataStore.getListingById(listingId);
        if (listing == null) {
            throw new ValidationException("Listing not found.");
        }
        if (!(listing instanceof SaleListing)) {
            throw new ValidationException("Only sale items support price bargaining.");
        }
        if (listing.getSeller().getUserId() == buyer.getUserId()) {
            throw new UnauthorizedOperationException("You cannot bargain on your own listing.");
        }
        if (initialOffer <= 0) {
            throw new ValidationException("Initial offer price must be greater than zero.");
        }
        SaleListing sl = (SaleListing) listing;
        if (initialOffer >= sl.getSellingPrice()) {
            throw new ValidationException("Offer must be lower than listed price of ₹" + sl.getSellingPrice() + ". Use direct purchase instead.");
        }

        // Check if there is already an active negotiation
        for (PriceNegotiation pn : dataStore.getPriceNegotiations()) {
            if (pn.getListingId() == listingId && pn.getBuyerId() == buyer.getUserId() &&
                    (pn.getStatus() == NegotiationStatus.PENDING_SELLER || pn.getStatus() == NegotiationStatus.COUNTERED_BY_SELLER)) {
                throw new ValidationException("You already have an active negotiation thread for this item.");
            }
        }

        int negId = dataStore.nextNegotiationId();
        PriceNegotiation pn = new PriceNegotiation(negId, listingId, listing.getProduct().getTitle(),
                sl.getSellingPrice(), buyer.getUserId(), buyer.getFullName(),
                listing.getSeller().getUserId(), listing.getSeller().getFullName(),
                initialOffer, buyerNote != null ? buyerNote.trim() : "Would like to offer ₹" + initialOffer);
        dataStore.addPriceNegotiation(pn);
        return pn;
    }

    public void sellerCounter(int negotiationId, int sellerId, double counterPrice, String note)
            throws UnauthorizedOperationException, ValidationException {
        PriceNegotiation pn = dataStore.getPriceNegotiationById(negotiationId);
        if (pn == null) throw new ValidationException("Negotiation not found.");
        if (pn.getSellerId() != sellerId) throw new UnauthorizedOperationException("Only the seller can counter-offer.");
        if (!pn.canCounter()) throw new ValidationException("Maximum negotiation rounds reached or thread closed.");
        if (counterPrice <= pn.getCurrentOfferPrice() || counterPrice > pn.getListedPrice()) {
            throw new ValidationException("Counter price must be between current offer (₹" + pn.getCurrentOfferPrice() + ") and listed price (₹" + pn.getListedPrice() + ").");
        }

        pn.sellerCounter(counterPrice, note);
    }

    public void sellerAccept(int negotiationId, int sellerId)
            throws UnauthorizedOperationException, ValidationException {
        PriceNegotiation pn = dataStore.getPriceNegotiationById(negotiationId);
        if (pn == null) throw new ValidationException("Negotiation not found.");
        if (pn.getSellerId() != sellerId) throw new UnauthorizedOperationException("Only the seller can accept this offer.");

        pn.accept();
    }

    public void sellerDecline(int negotiationId, int sellerId, String note)
            throws UnauthorizedOperationException, ValidationException {
        PriceNegotiation pn = dataStore.getPriceNegotiationById(negotiationId);
        if (pn == null) throw new ValidationException("Negotiation not found.");
        if (pn.getSellerId() != sellerId) throw new UnauthorizedOperationException("Only the seller can decline this offer.");

        pn.decline(note);
    }

    public void buyerAcceptCounter(int negotiationId, int buyerId)
            throws UnauthorizedOperationException, ValidationException {
        PriceNegotiation pn = dataStore.getPriceNegotiationById(negotiationId);
        if (pn == null) throw new ValidationException("Negotiation not found.");
        if (pn.getBuyerId() != buyerId) throw new UnauthorizedOperationException("Only the buyer can accept the counter-offer.");

        pn.accept();
    }

    public void buyerCounter(int negotiationId, int buyerId, double newOffer, String note)
            throws UnauthorizedOperationException, ValidationException {
        PriceNegotiation pn = dataStore.getPriceNegotiationById(negotiationId);
        if (pn == null) throw new ValidationException("Negotiation not found.");
        if (pn.getBuyerId() != buyerId) throw new UnauthorizedOperationException("Only the buyer can submit a counter.");
        if (!pn.canCounter()) throw new ValidationException("Maximum negotiation rounds reached or thread closed.");
        if (newOffer <= pn.getCurrentOfferPrice() || newOffer >= pn.getListedPrice()) {
            throw new ValidationException("New counter offer must be higher than previous offer but lower than listed price.");
        }

        pn.buyerCounter(newOffer, note);
    }

    public void buyerDecline(int negotiationId, int buyerId)
            throws UnauthorizedOperationException, ValidationException {
        PriceNegotiation pn = dataStore.getPriceNegotiationById(negotiationId);
        if (pn == null) throw new ValidationException("Negotiation not found.");
        if (pn.getBuyerId() != buyerId) throw new UnauthorizedOperationException("Only the buyer can decline.");

        pn.decline("Declined by buyer.");
    }

    public List<PriceNegotiation> getNegotiationsForUser(int userId) {
        return dataStore.getPriceNegotiations().stream()
                .filter(n -> n.getBuyerId() == userId || n.getSellerId() == userId)
                .collect(Collectors.toList());
    }

    public List<PriceNegotiation> getNegotiationsForListing(int listingId) {
        return dataStore.getPriceNegotiations().stream()
                .filter(n -> n.getListingId() == listingId)
                .collect(Collectors.toList());
    }
}

