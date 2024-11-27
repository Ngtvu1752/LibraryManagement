package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
            Scene loginScene = new Scene(loginRoot);
            SceneManage.addScene("Login", loginScene);

            Parent signUpRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/signUp.fxml")));
            Scene signUpScene = new Scene(signUpRoot);
            SceneManage.addScene("SignUp", signUpScene);
            SceneManage.printAvailableScenes();

            SceneController sceneController = SceneController.getInstance();
            sceneController.setStage(primaryStage);

            primaryStage.setScene(SceneManage.getScene("Login"));
            primaryStage.setTitle("Library Management");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Khởi động ứng dụng
    }
}
