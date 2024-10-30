package org.example;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {

    @FXML
    private TextField studentIdField; // Kết nối với TextField trong FXML

    @FXML
    private PasswordField passwordField; // Kết nối với PasswordField trong FXML

    @FXML
    private Button signInButton; // Kết nối với nút Sign in

    @FXML
    private Button signUpButton; // Kết nối với nút Sign up

    @FXML
    private Button forgotPasswordButton; // Kết nối với nút Forgot password

    private final DatabaseHelper dbHelper = new DatabaseHelper();
    // Phương thức khởi tạo
    @FXML
    public void initialize() {
        signInButton.setOnAction(event -> handleSignIn());
        signUpButton.setOnAction(event -> handleSignUp());
        forgotPasswordButton.setOnAction(event -> handleForgotPassword());
    }

    private void handleSignIn() {
        String studentId = studentIdField.getText();
        String password = passwordField.getText();

        // Xử lý đăng nhập
        if (studentId.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter Student ID and Password.");
        } else {
            // Kiểm tra thông tin đăng nhập (ví dụ: kiểm tra với cơ sở dữ liệu)
            if (isValidLogin(studentId, password)) {
                // Mở trang chính
                openHomePage();
                // Đóng trang đăng nhập
                closeLoginPage();
            } else {
                showAlert("Error", "Invalid Student ID or Password.");
            }
        }
    }

    private boolean isValidLogin(String studentId, String password) {
        // Kiểm tra thông tin đăng nhập với cơ sở dữ liệu hoặc danh sách người dùng
        System.out.println("Student ID: " + studentId);
        boolean valid = dbHelper.userExists(studentId, password);
        if(valid == true) {
            System.out.println("Student ID: " + studentId + " Password: " + password);
            return true;
        }
        else return false;
    }

    private void openHomePage() {
        // Mở trang chính (Home Page)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Home Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeLoginPage() {
        // Đóng trang đăng nhập
        Stage currentStage = (Stage) studentIdField.getScene().getWindow();
        currentStage.close();
    }

    private void handleSignUp() {
        try {
            // Tạo một stage mới
            Stage signUpStage = new Stage();

            // Tải file FXML cho giao diện đăng ký
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signUp.fxml"));
            Parent root = loader.load();
            System.out.println(getClass().getResource("/signUp.fxml")); // Kiểm tra xem có null hay không

            // Tạo một scene mới và gán cho stage
            Scene scene = new Scene(root);
            signUpStage.setScene(scene);

            // Thiết lập tiêu đề và hiển thị stage
            signUpStage.setTitle("Sign Up");
            signUpStage.show();
            
            // Nếu muốn ẩn cửa sổ hiện tại
            Stage currentStage = (Stage) signUpButton.getScene().getWindow(); // Dùng signUpButton
            currentStage.hide();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open sign up page.");
        }
    }
    private void handleForgotPassword() {
        // Xử lý quên mật khẩu (có thể mở một cửa sổ mới hoặc gửi yêu cầu đặt lại mật khẩu)
        showAlert("Info", "Redirecting to Forgot Password page...");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
