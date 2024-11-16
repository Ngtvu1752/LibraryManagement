package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Tải file FXML
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

        primaryStage.setTitle("Login Application");
        primaryStage.setScene(new Scene(root, 700, 538)); // Kích thước cửa sổ
        primaryStage.setResizable(false);
        primaryStage.show(); // Hiển thị cửa sổ
    }

    public static void main(String[] args) {
        launch(args); // Khởi động ứng dụng
    }
}
