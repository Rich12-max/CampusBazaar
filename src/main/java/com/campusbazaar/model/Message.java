package com.campusbazaar.model;

import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private int listingId;
    private String itemTitle;
    private int senderId;
    private String senderName;
    private int receiverId;
    private String receiverName;
    private String messageText;
    private boolean isRead;
    private LocalDateTime sentAt;

    public Message(int messageId, int listingId, String itemTitle, int senderId, String senderName,
                   int receiverId, String receiverName, String messageText, boolean isRead) {
        this.messageId = messageId;
        this.listingId = listingId;
        this.itemTitle = itemTitle;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.messageText = messageText;
        this.isRead = isRead;
        this.sentAt = LocalDateTime.now();
    }

    public int getMessageId() {
        return messageId;
    }

    public int getListingId() {
        return listingId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessageText() {
        return messageText;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}

