package com.campusbazaar.service;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.Message;

import java.util.List;
import java.util.stream.Collectors;

public class MessagingService {
    private final DataStore dataStore = DataStore.getInstance();

    public Message sendMessage(int listingId, String itemTitle, int senderId, String senderName,
                               int receiverId, String receiverName, String text) {
        int id = dataStore.nextMessageId();
        Message msg = new Message(id, listingId, itemTitle, senderId, senderName, receiverId, receiverName, text, false);
        dataStore.addMessage(msg);
        return msg;
    }

    public List<Message> getThread(int listingId, int user1, int user2) {
        return dataStore.getMessages().stream()
                .filter(m -> m.getListingId() == listingId &&
                        ((m.getSenderId() == user1 && m.getReceiverId() == user2) ||
                         (m.getSenderId() == user2 && m.getReceiverId() == user1)))
                .collect(Collectors.toList());
    }

    public List<Message> getInboxForUser(int userId) {
        return dataStore.getMessages().stream()
                .filter(m -> m.getReceiverId() == userId || m.getSenderId() == userId)
                .collect(Collectors.toList());
    }
}

