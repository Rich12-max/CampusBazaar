package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.ListingStatus;
import com.campusbazaar.model.enums.PaymentMethod;
import com.campusbazaar.model.enums.RequestStatus;
import com.campusbazaar.model.enums.TransactionStatus;

import java.util.List;
import java.util.stream.Collectors;

public class PurchaseService {
    private final DataStore dataStore = DataStore.getInstance();

    public PurchaseRequest submitPurchaseRequest(Listing listing, Student buyer, double offeredPrice,
                                                String proposedSpot, String notes) {
        int reqId = dataStore.nextRequestId();
        PurchaseRequest req = new PurchaseRequest(reqId, listing.getListingId(), buyer.getUserId(),
                buyer.getFullName(), offeredPrice, proposedSpot, notes, RequestStatus.PENDING);
        dataStore.addPurchaseRequest(req);
        return req;
    }

    public void acceptRequest(int requestId) {
        PurchaseRequest req = dataStore.getPurchaseRequestById(requestId);
        if (req != null) {
            req.setStatus(RequestStatus.ACCEPTED);
            Listing listing = dataStore.getListingById(req.getListingId());
            if (listing != null) {
                listing.setStatus(ListingStatus.RESERVED);
            }
        }
    }

    public void declineRequest(int requestId) {
        PurchaseRequest req = dataStore.getPurchaseRequestById(requestId);
        if (req != null) {
            req.setStatus(RequestStatus.DECLINED);
        }
    }

    public PurchaseTransaction completeHandover(int requestId, PaymentMethod paymentMethod) {
        PurchaseRequest req = dataStore.getPurchaseRequestById(requestId);
        if (req == null) return null;

        Listing listing = dataStore.getListingById(req.getListingId());
        if (listing == null) return null;

        req.setStatus(RequestStatus.COMPLETED);
        listing.setStatus(ListingStatus.SOLD);

        String code = "BUY-TXN-" + System.currentTimeMillis() % 100000;
        PurchaseTransaction txn = new PurchaseTransaction(dataStore.nextTransactionId(), code,
                listing.getListingId(), listing.getProduct().getTitle(),
                req.getBuyerId(), req.getBuyerName(),
                listing.getSeller().getUserId(), listing.getSeller().getFullName(),
                req.getOfferedPrice(), paymentMethod, TransactionStatus.COMPLETED,
                req.getProposedSpot());

        dataStore.addTransaction(txn);
        return txn;
    }

    public List<PurchaseRequest> getRequestsForSeller(int sellerId) {
        return dataStore.getPurchaseRequests().stream()
                .filter(pr -> {
                    Listing l = dataStore.getListingById(pr.getListingId());
                    return l != null && l.getSeller().getUserId() == sellerId;
                })
                .collect(Collectors.toList());
    }

    public List<PurchaseRequest> getRequestsForBuyer(int buyerId) {
        return dataStore.getPurchaseRequests().stream()
                .filter(pr -> pr.getBuyerId() == buyerId)
                .collect(Collectors.toList());
    }
}

