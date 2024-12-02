package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.example.*;
import org.example.Database.Student;
import org.example.Database.DAO.StudentDAO;

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

    private StudentDAO studentDAO = new StudentDAO();

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
        // Kiểm tra thông tin0

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || securityQuestion == null || answer.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
        } else {
            String hashedPassword = PasswordUtil.hashPassword(password);
            Student newStudent = new Student(username, hashedPassword, name, securityQuestion, answer);
            boolean isInserted = studentDAO.save(newStudent);
            if (isInserted) {
                showAlert("Success", "Account created successfully!");
                handleBackButton();
            } else {
                showAlert("Error", "Username already exists, please choose another name");
            }
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
        SceneController.getInstance().switchScene("Login");
        SceneManage.removeScene("SignUp");
    }
}
