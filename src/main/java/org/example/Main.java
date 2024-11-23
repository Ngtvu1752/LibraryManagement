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
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
            Scene homeScene = new Scene(homeRoot);
            SceneManage.addScene("HomePage", homeScene);

            Parent StudentHomePageRoot = FXMLLoader.load(getClass().getResource("/StudentHomePage.fxml"));
            Scene StudentHomePageScene = new Scene(StudentHomePageRoot);
            SceneManage.addScene("StudentHomePage", StudentHomePageScene);

            Parent loginRoot = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            SceneManage.addScene("Login", loginScene);

            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/signUp.fxml"));
            Scene signUpScene = new Scene(signUpRoot);
            SceneManage.addScene("SignUp", signUpScene);

            Parent manageBookRoot = FXMLLoader.load(getClass().getResource("/ManageBook.fxml"));
            Scene manageBookScene = new Scene(manageBookRoot);
            SceneManage.addScene("ManageBook", manageBookScene);

            Parent manageBookStudentRoot = FXMLLoader.load(getClass().getResource("/ManageBookStudent.fxml"));
            Scene manageBookStudentScene = new Scene(manageBookStudentRoot);
            SceneManage.addScene("ManageBookStudent", manageBookStudentScene);

            Parent manageStudentRoot = FXMLLoader.load(getClass().getResource("/ManageStudent.fxml"));
            Scene manageStudentScene = new Scene(manageStudentRoot);
            SceneManage.addScene("ManageStudent", manageStudentScene);

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
