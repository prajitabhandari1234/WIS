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
 * FXML Controller class
 *
 * @author acer
 */
public class PasswordController implements Initializable {


    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtOldPassword;
    @FXML
    private TextField txtNewPassword;
    @FXML
    private TextArea txtMessages;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnExit;
    /**
     * Initializes the controller class.
     */
    
    private SceneCoordinator sc;
    private UserDataManager udm;
    private UserDataValidator udv;
    
    public void inject(SceneCoordinator sc, UserDataManager udm, UserDataValidator udv) {
        this.sc = sc;
        this.udm = udm;
        this.udv = udv;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void submitAction(ActionEvent event) {
        txtMessages.setText("Submit: change password under development");
    }

    @FXML
    private void clearAction(ActionEvent event) {
        txtUserName.clear();
        txtOldPassword.clear();
        txtNewPassword.clear();
        txtMessages.clear();
    }

    @FXML
    private void exitAction(ActionEvent event) {
        sc.setScene(SceneKey.LOGIN);
    }

}
