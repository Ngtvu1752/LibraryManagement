package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    void handleManageBook(ActionEvent event) {
        System.out.println("Manage Book button clicked");
        // Điều hướng đến giao diện quản lý sách
    }
}
