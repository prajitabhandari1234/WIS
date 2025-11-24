/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author acer
 */
public class SceneCoordinator {
    public static enum SceneKey {
        LOGIN,
        PASSWORD,
        QUERY // Add more keys here if you have more scenes
    }

    private Stage stage;
    private HashMap<SceneKey, Scene> scenes = new HashMap<>();

    public SceneCoordinator(Stage stage) {
        this.stage = stage;
    }

    public void addScene(SceneKey key, Scene value) {
        scenes.put(key, value);
    }

    public void start() {
        setScene(SceneKey.LOGIN); // Set the initial scene
    }

    public void setScene(SceneKey key) {
        Scene scene = scenes.get(key);
        if (scene != null) {
            stage.setScene(scene);
            stage.setTitle("Whiskey Information System");
            stage.show();
        } else {
            System.err.println("Scene not found for key: " + key);
        }
    }
}