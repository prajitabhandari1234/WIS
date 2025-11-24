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
 * FXML Controller class
 *
 * @author acer
 */
public class LoginController implements Initializable {


    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextArea txtMessage;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnChangePassword;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnExit;
    
    private SceneCoordinator sc;
    private UserDataManager udm;
    private UserDataValidator udv;
    
    /**
     * Initializes the controller class.
     * @param sc
     * @param udm
     * @param udv
     */
    
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
    private void loginAction(ActionEvent event) {
        txtMessage.setText("Login logic: under development");
    }

    @FXML
    private void changePasswordAction(ActionEvent event) {
        sc.setScene(SceneKey.PASSWORD);
    }

    @FXML
    private void clearAction(ActionEvent event) {
        txtUserName.clear();
        txtPassword.clear();
        txtMessage.clear();
    }

    @FXML
    private void exitAction(ActionEvent event) {
        System.exit(0);
    }

}
