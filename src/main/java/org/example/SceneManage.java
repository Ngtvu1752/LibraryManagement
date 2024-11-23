package org.example;

import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class SceneManage {
    private static Map<String, Scene> scenes = new HashMap<>();

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public static Scene getScene(String name) {
        return scenes.get(name);
    }

    public static void removeScene(String name) {
        scenes.remove(name);
    }
}
