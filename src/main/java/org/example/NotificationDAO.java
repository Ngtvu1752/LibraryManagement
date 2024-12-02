package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class NotificationDAO implements DAO<Notification> {
    private final DatabaseHelper dbHelper;
    private static NotificationDAO instance;

    private NotificationDAO() {
        this.dbHelper = DatabaseHelper.getInstance();
    }
    public static NotificationDAO getInstance() {
        if (instance == null) {
            synchronized (NotificationDAO.class) {
                if (instance == null) {
                    instance = new NotificationDAO();
                }
            }
        }
        return instance;
    }

    public List<Notification> getAll() {
        List<Notification> notifications = null;
        return notifications;
    }

    public ObservableList<Notification> getObservableList() {
        ObservableList<Notification> notifications = FXCollections.observableArrayList();
        String sql = "SELECT * FROM notifications WHERE id = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, SessionManager.getCurrentUser().getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public boolean save(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, message, timestamp) VALUES (?, ?, ?)";
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getTimestamp()));
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Notification notification) {
        return false;
    }

    public boolean delete(Notification notification) {
        return false;
    }
}
