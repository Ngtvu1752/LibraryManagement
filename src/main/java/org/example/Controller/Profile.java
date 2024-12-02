package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.DatabaseHelper;
import org.example.SessionManager;

import java.sql.*;

public class Profile {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField studentIdField;
    @FXML
    private TextField classField;
    @FXML
    private TextField schoolField;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    private final DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    public void initialize() {
        loadUserProfile();
        saveButton.setOnAction(event -> saveUserProfile());
        backButton.setOnAction(event -> setBackButton());
    }

    private void loadUserProfile() {
        String query = "SELECT name, id, class, school FROM users WHERE id = ?";

        try (Connection conn = dbHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, SessionManager.getCurrentUser().getId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("name");
                int studentId = rs.getInt("id");
                String studentClass = rs.getString("class");
                String school = rs.getString("school");

                fullNameField.setText(fullName != null ? fullName : "");
                studentIdField.setText(String.valueOf(studentId));
                classField.setText(studentClass != null ? studentClass : "");
                schoolField.setText(school != null ? school : "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveUserProfile() {
        String updateQuery = "UPDATE users SET name = ?, class = ?, school = ? WHERE id = ?";

        try (Connection conn = dbHelper.connect();
        PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            String fullName = fullNameField.getText();
            String studentClass = classField.getText();
            String school = schoolField.getText();
            String studentId = studentIdField.getText(); // Giả sử studentId không được phép thay đổi

            pstmt.setString(1, fullName);
            pstmt.setString(2, studentClass);
            pstmt.setString(3, school);
            pstmt.setString(4, studentId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Profile updated successfully.");
                showAlert("Success", "Profile updated successfully.");
            } else {
                System.out.println("No changes made.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBackButton() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
