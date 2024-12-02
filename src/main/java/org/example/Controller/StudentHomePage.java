package org.example.Controller;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentHomePage {

    @FXML
    private Button manageBookButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox notificationPopup;

    @FXML
    private Text unreadText;

    @FXML
    private ListView<Notification> notificationList;

    private boolean isPopupVisible = false;

    private final DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    @FXML
    public void initialize() {
        int unRead = getUnreadNotificationCount();
        if (unRead > 0) {
            unreadText.setText( "(" + unRead + ")" );
        } else {
            unreadText.setText("(" + 0 + ")" );
        }
        notificationPopup.setVisible(false);
        notificationPopup.setManaged(false);
        manageBookButton.setOnAction(event -> handleManageBook());
        profileButton.setOnAction(event -> handleProfile());
        logoutButton.setOnAction(event -> handleLogout());
    }

    @FXML
    private void toggleNotificationPopup() {
        int unRead = getUnreadNotificationCount();
        if (unRead > 0) {
            unreadText.setText( "(" + unRead + ")" );
        } else {
            unreadText.setText("(" + 0 + ")" );
        }
        isPopupVisible = !isPopupVisible; // Đảo trạng thái
        notificationPopup.setVisible(isPopupVisible);
        notificationPopup.setManaged(isPopupVisible);
        if (isPopupVisible) {
            loadNotifications();
            FadeTransition fade = new FadeTransition(Duration.millis(300), notificationPopup);
            fade.setFromValue(0);
            fade.setToValue(1);
            markNotificationsAsRead();
            fade.play();
        }
    }

    private void loadNotifications() {
        int userId = SessionManager.getCurrentUser().getId();
        List<Notification> notifications = fetchNotificationsFromDB(userId);

        ObservableList<Notification> items = FXCollections.observableArrayList(notifications);
        notificationList.setItems(items);

        notificationList.setCellFactory(listView -> new ListCell<Notification>() {
            private final Label label = new Label();

            {
                label.setWrapText(true);
                label.setMaxWidth(180);
                label.setStyle("-fx-padding: 5px;");
            }

            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.getMessage());
                    if (item.isIs_read()) {
                        label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5px;");
                    } else {
                        label.setStyle("-fx-background-color: #ffd700; -fx-padding: 5px;"); // Màu cho thông báo chưa đọc
                    }
                    setGraphic(label);
                }
            }
        });
    }


    private List<Notification> fetchNotificationsFromDB(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT message, is_read FROM notifications WHERE user_id = ? ORDER BY timestamp DESC";
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(rs.getString("message"),  rs.getBoolean("is_read")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }


    private int getUnreadNotificationCount() {
        String query = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0";
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, SessionManager.getCurrentUser().getId());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void markNotificationsAsRead() {
        String query = "UPDATE notifications SET is_read = 1 WHERE user_id = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, SessionManager.getCurrentUser().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleManageBook() {
        if (SceneManage.getScene("ManageBookStudent") != null) {
            SceneController.getInstance().switchScene("ManageBookStudent");
        } else {
            try {
                Parent manageBookStudentRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ManageBookStudent.fxml")));
                Scene manageBookStudentScene = new Scene(manageBookStudentRoot);
                SceneManage.addScene("ManageBookStudent", manageBookStudentScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SceneController.getInstance().switchScene("ManageBookStudent");
        }
    }

    private void handleProfile() {
        try {
            openNewWindow("/Profile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        try {
            // Tải file FXML của cửa sổ Login
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Lấy Stage hiện tại (HomePage)
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();

            // Tạo Scene mới từ giao diện Login
            Scene loginScene = new Scene(loginRoot);

            // Hiển thị màn hình Login
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openNewWindow(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}
