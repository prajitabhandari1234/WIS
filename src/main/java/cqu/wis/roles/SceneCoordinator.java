/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages switching between different JavaFX scenes in the application.
 *
 * <p>
 * This coordinator acts as a central controller to manage and switch between UI
 * scenes such as login, password reset, and whiskey queries. Controllers do not
 * directly handle the stage—they call this class to display the required
 * scene.</p>
 *
 * <p>
 * All scenes must be registered before switching using
 * {@link #addScene(SceneKey, Scene)}.</p>
 *
 * @author Prajita Bhandari
 *
 */
public class SceneCoordinator {

    /**
     * Enumeration defining the available scenes in the application.
     *
     * <ul>
     * <li>{@code LOGIN} – the user login screen</li>
     * <li>{@code PASSWORD} – the password reset/change screen</li>
     * <li>{@code QUERY} – the whiskey query interface</li>
     * </ul>
     */
    public static enum SceneKey {
        LOGIN,
        PASSWORD,
        QUERY
    }

    /**
     * The primary JavaFX stage used for displaying scenes.
     */
    private final Stage stage;

    /**
     * A map of scenes indexed by their corresponding {@link SceneKey}.
     */
    private final Map<SceneKey, Scene> scenes = new HashMap<>();

    /**
     * Constructs a new SceneCoordinator with the provided JavaFX primary stage.
     *
     * @param stage The main window (JavaFX Stage) used by the application.
     * @throws NullPointerException If the provided {@code stage} is
     * {@code null}.
     */
    public SceneCoordinator(Stage stage) {
        if (stage == null) {
            throw new NullPointerException("Stage cannot be null");
        }
        this.stage = stage;
    }

    /**
     * Registers a new scene with the coordinator.
     *
     * <p>
     * Each scene must be uniquely associated with a {@link SceneKey}. Calling
     * this method with an already existing key will replace the previous scene
     * associated with it.</p>
     *
     * @param key The unique identifier (enum value) for the scene.
     * @param value The actual JavaFX {@link Scene} instance to register.
     * @throws NullPointerException If either {@code key} or {@code value} is
     * {@code null}.
     */
    public void addScene(SceneKey key, Scene value) {
        if (key == null || value == null) {
            throw new NullPointerException("SceneKey and Scene must not be null");
        }
        scenes.put(key, value);
    }

    /**
     * Starts the application by setting the initial scene to
     * {@link SceneKey#LOGIN}.
     *
     * @throws IllegalStateException If the LOGIN scene has not been registered.
     */
    public void start() {
        setScene(SceneKey.LOGIN);
    }

    /**
     * Switches the current scene being displayed to the one associated with the
     * specified key.
     *
     * <p>
     * This method sets the new scene on the JavaFX stage and brings it to the
     * foreground. If the specified scene was not registered using
     * {@link #addScene(SceneKey, Scene)}, a message will be printed to the
     * error output.</p>
     *
     * @param key The {@link SceneKey} that corresponds to the scene to display.
     * @throws NullPointerException If {@code key} is {@code null}.
     */
    public void setScene(SceneKey key) {
        if (key == null) {
            throw new NullPointerException("SceneKey must not be null");
        }
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
