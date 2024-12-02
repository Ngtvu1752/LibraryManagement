package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.SceneController;
import org.example.SceneManage;

import java.io.IOException;
import java.util.Objects;

public class HomePage {

    @FXML
    private Button manageStudentButton;

    @FXML
    private Button manageBookButton;

    @FXML
    private Button returnBookButton;

    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        // Thiết lập sự kiện cho các nút
        manageStudentButton.setOnAction(event -> handleManageStudent());
        manageBookButton.setOnAction(event -> handleManageBook());
        logoutButton.setOnAction(event -> handleLogout());
    }

    private void handleManageStudent() {
        if (SceneManage.getScene("ManageStudent") != null) {
            SceneController.getInstance().switchScene("ManageStudent");
        } else {
            try {
                Parent manageBookStudentRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ManageStudent.fxml")));
                Scene manageBookStudentScene = new Scene(manageBookStudentRoot);
                SceneManage.addScene("ManageStudent", manageBookStudentScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SceneController.getInstance().switchScene("ManageStudent");
        }
    }

    private void handleManageBook() {
        if (SceneManage.getScene("ManageBook") != null) {
            SceneController.getInstance().switchScene("ManageBook");
        } else {
            try {
                Parent manageBookStudentRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ManageBook.fxml")));
                Scene manageBookStudentScene = new Scene(manageBookStudentRoot);
                SceneManage.addScene("ManageBook", manageBookStudentScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SceneController.getInstance().switchScene("ManageBook");
        }
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ManageBook.fxml"));
//            Parent root = fxmlLoader.load();
//            Stage currentBookStage = (Stage) manageBookButton.getScene().getWindow();
//            Stage manageBookStage = new Stage();
//            manageBookStage.setScene(new Scene(root));
//            manageBookStage.show();
//
//            currentBookStage.hide();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
    }

    private void handleReturnBook() {
        System.out.println("Return Book button clicked");
    }

    private void handleLogout() {
        try {
            // Tải file FXML của cửa sổ Login
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Lấy Stage hiện tại (HomePage)
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();

            // Tạo Scene mới từ giao diện Login
            Scene loginScene = new Scene(loginRoot);

            // Hiển thị màn hình Login
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openNewWindow(String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
