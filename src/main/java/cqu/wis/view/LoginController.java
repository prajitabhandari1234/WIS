package cqu.wis.view;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import cqu.wis.roles.SceneCoordinator;
import cqu.wis.roles.SceneCoordinator.SceneKey;
import cqu.wis.roles.UserDataManager;
import cqu.wis.roles.UserDataValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class for the login interface of the whiskey information
 * system.
 * <p>
 * Manages user authentication operations including login validation, password
 * changes, and scene navigation. Implements the Model-View-Controller pattern
 * by coordinating between the user interface components and the business logic
 * layers.
 * </p>
 *
 * @author Prajita Bhandari
 *
 */
public class LoginController implements Initializable {

    /**
     * Text field for username input.
     */
    @FXML
    private TextField txtUserName;

    /**
     * Text field for password input.
     */
    @FXML
    private TextField txtPassword;

    /**
     * Text area for displaying system messages and feedback to users.
     */
    @FXML
    private TextArea txtMessage;

    /**
     * Button to trigger login authentication process.
     */
    @FXML
    private Button btnLogin;

    /**
     * Button to navigate to password change interface.
     */
    @FXML
    private Button btnChangePassword;

    /**
     * Button to clear all input fields and messages.
     */
    @FXML
    private Button btnClear;

    /**
     * Button to exit the application.
     */
    @FXML
    private Button btnExit;

    /**
     * Coordinator for managing scene transitions.
     */
    private SceneCoordinator sc;

    /**
     * Manager for handling user data operations such as lookup and password
     * updates.
     */
    private UserDataManager udm;

    /**
     * Validator for user data inputs including login and password change
     * validations.
     */
    private UserDataValidator udv;

    /**
     * Injects required dependencies into the controller for proper operation.
     * Must be called after controller instantiation and before any user
     * interactions.
     *
     * @param sc SceneCoordinator instance for managing scene transitions.
     * @param udm UserDataManager instance for user data operations and
     * authentication.
     * @param udv UserDataValidator instance for input and credential
     * validation.
     */
    public void inject(SceneCoordinator sc, UserDataManager udm, UserDataValidator udv) {
        this.sc = sc;
        this.udm = udm;
        this.udv = udv;
    }

    /**
     * Initializes the controller class after FXML loading. Called automatically
     * by the FXMLLoader.
     *
     * Enhancements: - Enables text wrapping in txtMessage - Sets preferred row
     * count to show full messages without scroll
     *
     * @param url The location used to resolve relative paths (unused).
     * @param rb The resources used to localize the root object (unused).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtMessage.setWrapText(true);
        txtMessage.setPrefRowCount(4); // Ensure enough space for full messages
    }

    /**
     * Handles the login button action event to authenticate user credentials.
     * Implements multi‐stage validation, including default‐password redirect.
     *
     * 1) Check that username and password fields are present. 2) Retrieve user
     * details; if null ⇒ show "Username not found.". 3) If stored password ==
     * "password" ⇒ show "Default password, please change." and navigate to
     * PASSWORD scene (force change). 4) Otherwise compare SHA‐1(enteredPass) vs
     * stored hash: • If mismatch ⇒ show "Incorrect password." • If match ⇒
     * navigate to QUERY scene.
     *
     * @param event The {@link ActionEvent} triggered by clicking the login
     * button.
     */
    @FXML
    private void loginAction(ActionEvent event) {
        String username = txtUserName.getText();
        String password = txtPassword.getText();

        // 1) Validate that required fields are present
        var fieldCheck = udv.checkForFieldsPresent(username, password);
        if (!fieldCheck.valid()) {
            txtMessage.setText(fieldCheck.message());
            return;
        }

        // 2) Retrieve user details
        var userDetails = udm.findUser(username);

        // 3) Validate credentials (including default‐password logic)
        var authCheck = udv.checkCurrentDetails(userDetails, username, password);
        txtMessage.setText(authCheck.message());

        if (!authCheck.valid()) {
            // Invalid login or "Username not found."
            return;
        }

        // If valid AND default‐password prompt, redirect to PASSWORD scene
        if ("Default password, please change.".equals(authCheck.message())) {
            sc.setScene(SceneKey.PASSWORD);
            return;
        }

        // Otherwise (correct hashed password), proceed to QUERY
        sc.setScene(SceneKey.QUERY);
    }

    /**
     * Handles the change‐password button action event. Navigates the user to
     * the password‐change interface.
     *
     * @param event The {@link ActionEvent} triggered by clicking the
     * change‐password button.
     */
    @FXML
    private void changePasswordAction(ActionEvent event) {
        sc.setScene(SceneKey.PASSWORD);
    }

    /**
     * Handles the clear button action event to reset the login form.
     *
     * @param event The {@link ActionEvent} triggered by clicking the clear
     * button.
     */
    @FXML
    private void clearAction(ActionEvent event) {
        txtUserName.clear();
        txtPassword.clear();
        txtMessage.clear();
    }

    /**
     * Handles the exit button action event to terminate the application. Uses
     * System.exit(0) for a clean shutdown.
     *
     * @param event The {@link ActionEvent} triggered by clicking the exit
     * button.
     */
    @FXML
    private void exitAction(ActionEvent event) {
        System.exit(0);
    }
}
