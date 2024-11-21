package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageBookStudent {

    public Label isbnLabel;

    @FXML
    private TextField isbnField;

    @FXML
    private Button issueBookButton;
    @FXML
    private Button backButton;

    public void initialize() {
        isbnField.setOnMouseClicked(event -> {
            if (isbnField.getText().isEmpty()) {
                isbnLabel.setVisible(false);  // Ẩn Label khi người dùng click vào TextField
            }
        });

        isbnField.setOnKeyPressed(event -> {
            if (!isbnField.getText().isEmpty()) {
                isbnLabel.setVisible(false);  // Ẩn Label khi có văn bản trong TextField
            }
        });
        isbnField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && isbnField.getText().isEmpty()) {  // Khi mất tiêu điểm và TextField trống
                isbnLabel.setVisible(true);  // Hiện Label khi TextField trống và mất tiêu điểm
            }
        });
        issueBookButton.setOnAction(event -> handleIssueBook());
        backButton.setOnAction(event -> handleBackButton());
    }

    private void handleIssueBook() {
        User studentId = SessionManager.getCurrentUser();
        int userId = studentId.getId();
        String isbn = isbnField.getText();
        IssueBook issueBook = new IssueBook();
        boolean success = issueBook.borrowBook(userId, isbn);
        if (success) {
            // thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Book borrowed successfully!");
            alert.showAndWait();
        } else {
            // Sách không thể mượn.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Book is not available for borrowing.");
            alert.showAndWait();
        }
    }
    private void handleBackButton() {
        try {
            // Tải file FXML của cửa sổ
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/StudentHomePage.fxml"));
            Parent studentHomePageRoot = fxmlLoader.load();

            // Lấy Stage hiện tại (MangeBookStudent)
            Stage currentStage = (Stage) backButton.getScene().getWindow();

            // Tạo Scene mới từ giao diện StudentHomePage
            Scene studentHomePageScene = new Scene(studentHomePageRoot);

            // Hiển thị màn hình Login
            currentStage.setScene(studentHomePageScene);
            currentStage.setTitle("StudentHomePage");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
