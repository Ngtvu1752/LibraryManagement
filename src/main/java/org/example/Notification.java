package org.example;

import java.time.LocalDateTime;

public class Notification {
    private int userId;
    private String message;
    private LocalDateTime timestamp;
    private boolean is_read;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Notification(int userId, String message, LocalDateTime timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.is_read = false;
    }

    public Notification( String message, boolean is_read) {
        this.message = message;
        this.is_read = is_read;
    }
}
