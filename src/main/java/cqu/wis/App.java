package cqu.wis;

import cqu.wis.data.UserData;
import cqu.wis.roles.SceneCoordinator;
import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.SceneCoordinator.SceneKey;
import cqu.wis.roles.UserDataManager;
import cqu.wis.roles.UserDataValidator;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.WhiskeyDataValidator;
import cqu.wis.view.LoginController;
import cqu.wis.view.PasswordController;
import cqu.wis.view.QueryController;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Main application class for the Whiskey Information System (WIS).
 *
 * <p>
 * This class initializes and launches the JavaFX GUI. It establishes
 * connections to the Whiskey and User databases, creates data managers and
 * validators, and registers scenes using the SceneCoordinator.</p>
 *
 * <p>
 * If the application fails to connect to a database or load FXML scenes, it
 * will display an error alert and terminate gracefully.</p>
 *
 * @author Prajita Bhandari
 */
public class App extends Application {

    /**
     * Path to the directory containing all FXML layout files. This constant is
     * used to build full resource paths for scene loading.
     */
    public static final String FXML_PATH = "/cqu/wis/view";

    /**
     * The main entry point for the application.
     *
     * <p>
     * Calls the JavaFX launch method to begin the application lifecycle.</p>
     *
     * @param args Command-line arguments passed during startup (not used).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Called automatically by the JavaFX runtime during application launch.
     *
     * <p>
     * This method performs the following:</p>
     * <ul>
     * <li>Connects to Whiskey and User databases.</li>
     * <li>Instantiates corresponding data managers and validators.</li>
     * <li>Loads and registers FXML-based scenes for login, query, and password
     * reset.</li>
     * <li>Starts the application by showing the login screen.</li>
     * </ul>
     *
     * @param stage The primary window (Stage) provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage stage) {
        SceneCoordinator sc = new SceneCoordinator(stage);

        // Create and connect the data sources
        WhiskeyData wd = new WhiskeyData();
        UserData ud = new UserData();

        try {
            wd.connect();
        } catch (SQLException e) {
            showAlertAndExit("Cannot connect to WHISKEY database:\n" + e.getMessage());
            return;
        }

        try {
            ud.connect();
        } catch (SQLException e) {
            showAlertAndExit("Cannot connect to USERS database:\n" + e.getMessage());
            return;
        }

        // Create data managers and validators
        WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
        WhiskeyDataValidator wdv = new WhiskeyDataValidator();
        UserDataManager udm = new UserDataManager(ud);
        UserDataValidator udv = new UserDataValidator();

        try {
            // Load Query scene
            Scene queryScene = makeScene(SceneKey.QUERY);
            QueryController qc = (QueryController) queryScene.getUserData();
            qc.inject(sc, wdm, wdv);
            sc.addScene(SceneKey.QUERY, queryScene);

            // Load Login scene
            Scene loginScene = makeScene(SceneKey.LOGIN);
            LoginController lc = (LoginController) loginScene.getUserData();
            lc.inject(sc, udm, udv);
            sc.addScene(SceneKey.LOGIN, loginScene);

            // Load Password scene
            Scene passwordScene = makeScene(SceneKey.PASSWORD);
            PasswordController pc = (PasswordController) passwordScene.getUserData();
            pc.inject(sc, udm, udv);
            sc.addScene(SceneKey.PASSWORD, passwordScene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertAndExit("Failed to load UI:\n" + e.getMessage());
            return;
        }

        // Show the login screen
        sc.start();
    }

    /**
     * Loads an FXML file corresponding to the specified scene key and returns
     * the resulting Scene object.
     *
     * <p>
     * The controller instance for the FXML is stored in the Scene's userData
     * for later access and injection.</p>
     *
     * @param key A SceneKey enum value representing the scene to load (LOGIN,
     * QUERY, PASSWORD).
     * @return The loaded JavaFX Scene associated with the FXML layout and its
     * controller.
     * @throws Exception If the FXML resource cannot be found, loaded, or
     * parsed.
     */
    private static Scene makeScene(SceneKey key) throws Exception {
        String fxml = FXML_PATH + "/" + key.name().toLowerCase() + ".fxml";
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        scene.setUserData(loader.getController());
        return scene;
    }

    /**
     * Displays an error alert with a custom message and then terminates the
     * application gracefully.
     *
     * <p>
     * This method is used during startup failures such as:</p>
     * <ul>
     * <li>Database connection errors</li>
     * <li>FXML loading issues</li>
     * </ul>
     *
     * <p>
     * Graceful exit ensures Maven does not mark the run as a build failure.</p>
     *
     * @param message A string message to show in the error alert dialog.
     */
    private void showAlertAndExit(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Startup Error");
        alert.setContentText(message);
        alert.showAndWait();
        Platform.exit(); // Exits JavaFX
        System.exit(0);  // Prevents Maven "BUILD FAILURE"
    }
}
