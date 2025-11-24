/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cqu.wis.view;

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
 * FXML Controller class for the password change interface of the whiskey
 * information system.
 * <p>
 * Manages secure password modification operations including authentication
 * verification, password validation, and database updates. Implements
 * comprehensive security measures by validating current credentials before
 * allowing password changes and ensuring new passwords meet strength
 * requirements. Provides user feedback throughout the password change process
 * and handles navigation back to the login interface.
 * </p>
 *
 * @author Prajita Bhandari
 *
 */
public class PasswordController implements Initializable {

    /**
     * Text field for username input to identify the account for password
     * change.
     */
    @FXML
    private TextField txtUserName;

    /**
     * Text field for current password input to authenticate the user.
     */
    @FXML
    private TextField txtOldPassword;

    /**
     * Text field for new password input to replace the current password.
     */
    @FXML
    private TextField txtNewPassword;

    /**
     * Text area for displaying system messages, validation errors, and success
     * confirmations.
     */
    @FXML
    private TextArea txtMessages;

    /**
     * Button to submit the password change request after validation.
     */
    @FXML
    private Button btnSubmit;

    /**
     * Button to clear all input fields and messages.
     */
    @FXML
    private Button btnClear;

    /**
     * Button to exit the password change interface and return to login.
     */
    @FXML
    private Button btnExit;

    /**
     * Coordinator for handling scene transitions.
     */
    private SceneCoordinator sc;

    /**
     * Manager for executing password update logic and user lookups.
     */
    private UserDataManager udm;

    /**
     * Validator for user input and credential verification.
     */
    private UserDataValidator udv;

    /**
     * Injects required dependencies into the controller for proper operation.
     * Must be called after controller instantiation and before any user
     * interactions.
     *
     * @param sc SceneCoordinator instance for managing scene transitions.
     * @param udm UserDataManager instance for user data operations and password
     * updates.
     * @param udv UserDataValidator instance for credential and password
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
        txtMessages.setWrapText(true);
        txtMessages.setPrefRowCount(4); // Ensure enough space for full messages
    }

    /**
     * Handles the Submit button click to process a password change.
     *
     * Steps: 1) Check that all three fields (username, old password, new
     * password) are present. 2) Verify the old password against what's in the
     * database. 3) Validate the new password (complexity, difference from old,
     * etc.). 4) If everything is valid, update the DB with SHA‐1(newPassword)
     * and navigate to QUERY.
     *
     * @param event The {@link ActionEvent} triggered by clicking the Submit
     * button.
     */
    @FXML
    private void submitAction(ActionEvent event) {
        // Read user‐entered fields
        String username = txtUserName.getText().trim();
        String oldPassword = txtOldPassword.getText();
        String newPassword = txtNewPassword.getText();

        // 1) Field presence check
        var fieldCheck = udv.checkForFieldsPresent(username, oldPassword, newPassword);
        if (!fieldCheck.valid()) {
            txtMessages.setText(fieldCheck.message());
            return;
        }

        // 2) Authenticate current credentials
        var user = udm.findUser(username);
        var authCheck = udv.checkCurrentDetails(user, username, oldPassword);
        if (!authCheck.valid()) {
            txtMessages.setText(authCheck.message());
            return;
        }

        // If the stored password was "password", the validator would have returned
        // (true, "Default password, please change."), but we never reach here
        // if default‐password. In practice, user cannot be on this screen with default pw,
        // because LoginController redirected to PASSWORD only after seeing default pw.
        // 3) Validate new password strength & difference from old
        var newPassCheck = udv.checkNewDetails(user, oldPassword, newPassword, newPassword);
        if (!newPassCheck.valid()) {
            txtMessages.setText(newPassCheck.message());
            return;
        }

        // 4) All checks passed → update the password to its SHA‐1 hash
        udm.updatePassword(username, udv.generateSHA1(newPassword));
        txtMessages.setText("Password changed successfully.");

        // 5) Once updated, navigate back to the QUERY screen
        sc.setScene(SceneKey.QUERY);
    }

    /**
     * Handles the Clear button click to reset the form. Clears the username,
     * old password, new password fields, and any messages.
     *
     * @param event The {@link ActionEvent} triggered by clicking the Clear
     * button.
     */
    @FXML
    private void clearAction(ActionEvent event) {
        txtUserName.clear();
        txtOldPassword.clear();
        txtNewPassword.clear();
        txtMessages.clear();
    }

    /**
     * Handles the Exit button click to return to the login screen.
     *
     * @param event The {@link ActionEvent} triggered by clicking the Exit
     * button.
     */
    @FXML
    private void exitAction(ActionEvent event) {
        sc.setScene(SceneKey.LOGIN);
    }
}
