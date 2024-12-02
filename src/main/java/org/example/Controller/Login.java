package org.example.Controller;

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
import org.example.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

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

    private DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    // Phương thức khởi tạo
    @FXML
    public void initialize() {
        signInButton.setOnAction(event -> handleSignIn());
        signUpButton.setOnAction(event -> handleSignUp());
        forgotPasswordButton.setOnAction(event -> handleForgotPassword());

        studentIdField.setOnAction(event -> handleSignIn());
        passwordField.setOnAction(event -> handleSignIn());
    }

    private void handleSignIn() {
        String studentId = studentIdField.getText();
        String password = passwordField.getText();

        // Xử lý đăng nhập
        if (studentId.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter Student ID and Password.");
        } else {
            User user = authenticate(studentId, password);
            if (user != null) {
                SessionManager.setCurrentUser(user);
                if (user.getRole().equals("student")) {
                    openHomePageStudent();
                } else {
                    openHomePageAdmin();
                }
            } else {
                showAlert("Error", "Invalid Student ID or Password.");
            }
        }
    }

    private User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? /*AND password = ?*/";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
//            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                String name = rs.getString("name");
                int id = rs.getInt("id");
                if (storedPassword.equals(password) || PasswordUtil.checkPassword(password, storedPassword)) {
                    if (role.equals("admin")) {
                        return new Admin(username, password, name, question, answer);
                    } else if (role.equals("student")) {
                        return new Student(id, username, password, name, question, answer);
                    }
                } else {
                    System.out.println("sai mk");
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private boolean isValidLogin(String studentId, String password) {
        // Kiểm tra thông tin đăng nhập với cơ sở dữ liệu hoặc danh sách người dùng
        System.out.println("Student ID: " + studentId);
        boolean valid = dbHelper.userExists(studentId, password);
        if (valid == true) {
            System.out.println("Student ID: " + studentId + " Password: " + password);
            return true;
        } else return false;
    }

    private void openHomePageAdmin() {
        try {
            Parent homeRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/HomePage.fxml")));
            Scene homeScene = new Scene(homeRoot);
            SceneManage.addScene("HomePage", homeScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SceneController.getInstance().switchScene("HomePage");
    }

    private void openHomePageStudent() {
        try {
            Parent StudentHomePageRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/StudentHomePage.fxml")));
            Scene StudentHomePageScene = new Scene(StudentHomePageRoot);
            SceneManage.addScene("StudentHomePage", StudentHomePageScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SceneController.getInstance().switchScene("StudentHomePage");
    }


    private void handleSignUp() {
        if (SceneManage.getScene("SignUp") != null) {
            SceneController.getInstance().switchScene("SignUp");
        } else {
            try {
                Parent manageBookStudentRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/signUp.fxml")));
                Scene manageBookStudentScene = new Scene(manageBookStudentRoot);
                SceneManage.addScene("SignUp", manageBookStudentScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SceneController.getInstance().switchScene("SignUp");
        }
    }

    private void handleForgotPassword() {
        // Xử lý quên mật khẩu (có thể mở một cửa sổ mới hoặc gửi yêu cầu đặt lại mật khẩu)
        showAlert("Info", "Redirecting to Forgot Password page...");
        try {
            Stage forgotPasswordStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forgotPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            SceneManage.addScene("ForgotPassword", scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Forgot Password page.");
        }
        SceneController.getInstance().switchScene("ForgotPassword");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
