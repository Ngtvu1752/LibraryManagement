package org.example.Database;

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

    /**
     * Constructor 1.
     * Ban đầu, thông báo được đánh dấu là chưa đọc.
     *
     * @param userId    ID của người dùng mà thông báo được gửi đến.
     * @param message   nội dung tin nhắn của thông báo.
     * @param timestamp thời gian khi thông báo được tạo.
     */
    public Notification(int userId, String message, LocalDateTime timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.is_read = false;
    }

    /**
     * Constructor 2.
     *
     * @param message nội dung tin nhắn của thông báo.
     * @param is_read trạng thái đã đọc của thông báo.
     */
    public Notification(String message, boolean is_read) {
        this.message = message;
        this.is_read = is_read;
    }
}
