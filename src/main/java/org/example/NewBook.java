package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NewBook {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField languageField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField subjectField;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    @FXML
    public void initialize() {
        saveButton.setOnAction(event -> saveBookToDatabase());
        closeButton.setOnAction(event -> closeWindow());
    }

    private void saveBookToDatabase() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String language = languageField.getText();
        String subject = subjectField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        String sql = "INSERT INTO BOOK (ISBN, TITLE, AUTHOR, SUBJECT, LANGUAGE, QUANTITY, isAvail, Borrowed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, subject);
            pstmt.setString(5, language);
            pstmt.setInt(6, quantity);
            pstmt.setInt(7, quantity); // `isAvail` sẽ khởi tạo bằng tổng số lượng
            pstmt.setInt(8, 0); // `Borrowed` khởi tạo bằng 0 vì sách chưa bị mượn

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Hiển thị hộp thoại thành công
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book saved successfully!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        // Lấy Stage hiện tại của cửa sổ NewBook và đóng nó
        Stage currentStage = (Stage) closeButton.getScene().getWindow();
        currentStage.close();
    }
}
