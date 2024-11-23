package org.example;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;
    private static SceneController instance;

    private SceneController() {

    }

    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(String sceneName) {
        Scene scene = SceneManage.getScene(sceneName);
        if(scene != null) {
            stage.setScene(scene);
        } else {
            System.out.println("Scene not found" + sceneName);
        }
    }
}
