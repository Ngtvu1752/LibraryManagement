package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class ForgotPassword {

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField securityQuestionField;

    @FXML
    private TextField answerField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private Button searchButton;

    @FXML
    private Button retrieveButton;

    @FXML
    private Button backButton;

    private DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    // Initialize method to bind actions to buttons
    @FXML
    public void initialize() {
        searchButton.setOnAction(event -> handleSearch());
        retrieveButton.setOnAction(event -> handleRetrieve());
        backButton.setOnAction(event -> handleBackButton());
    }

    private void handleSearch() {
        String studentId = studentIdField.getText();

        if (studentId.isEmpty()) {
            showAlert("Error", "Please enter your Student ID.");
        } else {
            Student student = getStudentDetails(studentId);
            if (student != null) {
                nameField.setText(student.getName());
                securityQuestionField.setText(student.getSecurityQuestion());
            } else {
                showAlert("Error", "Student not found.");
            }
        }
    }

    private void handleRetrieve() {
        String studentId = studentIdField.getText();
        String answer = answerField.getText();

        if (studentId.isEmpty() || answer.isEmpty()) {
            showAlert("Error", "Please provide both the Student ID and your answer.");
        } else {
            Student student = getStudentDetails(studentId);
            if (student != null && student.getAnswer().equals(answer)) {
                String newPassword = generateRandomPassword();
                updatePassword(studentId, newPassword);
                newPasswordField.setText(newPassword);
                showAlert("Success", "Your password has been updated.");
            } else {
                showAlert("Error", "Incorrect answer to the security question.");
            }
        }
    }

    // Lấy thông tin sinh viên
    private Student getStudentDetails(String studentId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String securityQuestion = rs.getString("question");
                String securityAnswer = rs.getString("answer");
                return new Student(rs.getInt("id"), studentId, rs.getString("password"), name, securityQuestion, securityAnswer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // random password
    private String generateRandomPassword() {
        String characters = "0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    // Cập nhật mật khẩu trong database.
    private void updatePassword(String studentId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void handleBackButton() {
        SceneController.getInstance().switchScene("Login");
        SceneManage.removeScene("ForgotPassword");
    }

    // Show an alert to the user
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
