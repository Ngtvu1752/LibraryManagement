package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentHomePage {

    @FXML
    private Button returnBookButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button manageBookButton;

    @FXML
    void handleReturnBook(ActionEvent event) {
        System.out.println("Return Book button clicked");
        // Điều hướng đến giao diện trả sách
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            // Load file FXML của cửa sổ Login
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Lấy Stage hiện tại (HomePage)
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();

            // Tạo một Scene mới cho cửa sổ Login
            Scene loginScene = new Scene(loginRoot);

            // Hiển thị màn hình Login
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageBook() {
        try {
            // Lấy Stage hiện tại (HomePage)
            Stage currentStage = (Stage) manageBookButton.getScene().getWindow();

            // Tải FXML và mở cửa sổ "Manage Book"
            openNewWindow("/ManageBookStudent.fxml", "Manage Books Student");

            // Đóng cửa sổ hiện tại (HomePage)
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openNewWindow(String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
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
