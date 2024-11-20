package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ManageBookStudent {

    public Label isbnLabel;

    @FXML
    private TextField isbnField;

    @FXML
    private Button issueBookButton;

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
    }

    private void handleIssueBook() {
        User studentId = SessionManager.getCurrentUser();
        int userId = Integer.parseInt(studentId.username);
        String isbn = isbnField.getText();

        IssueBook issueBook = new IssueBook();

        // mượn cuốn sách
        boolean success = issueBook.borrowBook(userId, isbn);

        // Hiển thị hộp thoại
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

}
