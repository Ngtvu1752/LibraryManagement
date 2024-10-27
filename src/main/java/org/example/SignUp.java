package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUp {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> securityQuestionComboBox;

    @FXML
    private TextField answerField;

    @FXML
    private Button createButton;

    @FXML
    private Button backButton;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        // Thêm các câu hỏi bảo mật vào ComboBox
        securityQuestionComboBox.getItems().addAll(
                "What is your mother's name?",
                "What was the name of your first pet?",
                "What is your favorite book?",
                "What is your school's name?"
        );

        // Xử lý sự kiện khi nhấn nút "Create"
        createButton.setOnAction(event -> handleCreateButton());

        // Xử lý sự kiện khi nhấn nút "Back"
        backButton.setOnAction(event -> handleBackButton());
    }

    private void handleCreateButton() {
        // Lấy thông tin từ các trường
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String securityQuestion = securityQuestionComboBox.getValue();
        String answer = answerField.getText();

        // Kiểm tra thông tin
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || securityQuestion == null || answer.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
        } else {
            // Xử lý logic tạo tài khoản ở đây
            showAlert("Success", "Account created successfully!");
            handleBackButton();
            // Thêm mã để lưu tài khoản vào cơ sở dữ liệu hoặc file
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleBackButton() {
        try {
            // Tải FXML cho màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Lấy stage hiện tại và đặt lại cảnh
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        }
    }
}
