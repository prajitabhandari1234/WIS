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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    public static final String FXML_PATH = "/cqu/wis/view";
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        SceneCoordinator sc = new SceneCoordinator(stage);
        try {
            // Create data access objects
            WhiskeyData wd = new WhiskeyData();
            UserData ud = new UserData();
            wd.connect();
            ud.connect();
            WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
            WhiskeyDataValidator wdv = new WhiskeyDataValidator();
            UserDataManager udm = new UserDataManager(ud);
            UserDataValidator udv = new UserDataValidator();
            // Query Scene
            Scene queryScene = makeScene(SceneKey.QUERY);
            QueryController qc = (QueryController) queryScene.getUserData();
            qc.inject(sc, wdm, wdv);
            sc.addScene(SceneKey.QUERY, queryScene);
            // Login Scene
            Scene loginScene = makeScene(SceneKey.LOGIN);
            LoginController lc = (LoginController) loginScene.getUserData();
            lc.inject(sc, udm, udv);
            sc.addScene(SceneKey.LOGIN, loginScene);
            // Password Scene
            Scene passwordScene = makeScene(SceneKey.PASSWORD);
            PasswordController pc = (PasswordController) passwordScene.getUserData();
            pc.inject(sc, udm, udv);
            sc.addScene(SceneKey.PASSWORD, passwordScene);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        sc.start();
    }
    private static Scene makeScene(SceneKey key) throws Exception {
        String fxml = FXML_PATH + "/" + key.name().toLowerCase() + ".fxml";
        System.out.println("Loading: " + fxml);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        scene.setUserData(loader.getController());
        return scene;
    }
}