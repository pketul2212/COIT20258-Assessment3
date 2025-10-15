package model;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private String recipientId;
    private String message;
    private String type;
    private LocalDateTime sendTime;
    private boolean isRead;

    public Notification(String recipientId, String message, String type) {
        this.recipientId = recipientId;
        this.message = message;
        this.type = type;
        this.sendTime = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() { this.isRead = true; }
    public boolean send() { return true; }

    @Override
    public String toString() {
        return "Notification{" + type + ": " + message + "}";
    }
}
