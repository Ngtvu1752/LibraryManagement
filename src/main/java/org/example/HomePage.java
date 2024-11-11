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

public class HomePage {

    @FXML
    private Button newBookButton;

    @FXML
    private Button statisticButton;

    @FXML
    private Button returnBookButton;

    @FXML
    private Button findBookButton;

    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        newBookButton.setOnAction(event -> handleNewBook());
        statisticButton.setOnAction(event -> handleStatistic());
        returnBookButton.setOnAction(event -> handleReturnBook());
        findBookButton.setOnAction(event -> handleFindBook());
        logoutButton.setOnAction(event -> handleLogout());
    }

    private void handleNewBook() {
        User currentUser = SessionManager.getCurrentUser();
        if (!currentUser.getRole().equals("admin")) {
            showAlert("Access Denied", "Only Admin can add books.");
            return;
        }
        try {
            // Load file FXML của cửa sổ NewBook
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NewBook.fxml"));
            Parent newBookRoot = fxmlLoader.load();

            // Tạo một Stage mới cho cửa sổ NewBook
            Stage newBookStage = new Stage();
            newBookStage.setTitle("New Book");
            newBookStage.initModality(Modality.APPLICATION_MODAL); // Chặn các tương tác với cửa sổ khác
            newBookStage.setScene(new Scene(newBookRoot));
            newBookStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleStatistic() {
        System.out.println("Statistic button clicked");
    }

    private void handleReturnBook() {
        System.out.println("Return Book button clicked");
    }

    private void handleFindBook() {
        System.out.println("Find Book button clicked");
    }

    private void handleLogout() {
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
