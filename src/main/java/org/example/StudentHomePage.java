package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StudentHomePage {

    @FXML
    private Button manageBookButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        // Thiết lập sự kiện cho các nút
        manageBookButton.setOnAction(event -> handleManageBook());
        profileButton.setOnAction(event -> handleProfile());
        logoutButton.setOnAction(event -> handleLogout());

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
