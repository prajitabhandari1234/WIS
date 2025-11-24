/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cqu.wis.view;


import cqu.wis.roles.SceneCoordinator;
import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.WhiskeyDataValidator;
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
public class QueryController implements Initializable {


    @FXML
    private TextField txtDistillery;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtRegion;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextArea txtMessage;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnMaltsFromRegion;
    @FXML
    private Button btnMaltsInAgeRange;
    @FXML
    private TextField txtMaltsFromRegion;
    @FXML
    private TextField txtLowerAge;
    @FXML
    private TextField txtUpperAge;
    @FXML
    private Button btnAllMalts;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnExit;
    
    
    private SceneCoordinator sc;
    private WhiskeyDataManager wdm;
    private WhiskeyDataValidator wdv;
    
    public void inject(SceneCoordinator sc, WhiskeyDataManager wdm, WhiskeyDataValidator wdv) {
        this.sc = sc;
        this.wdm = wdm;
        this.wdv = wdv;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: Initialization logic (if needed)
    }    
    
    @FXML
    private void nextAction(ActionEvent event) {
        display(wdm.next());
    }

    @FXML
    private void previousAction(ActionEvent event) {
        display(wdm.previous());
    }

    @FXML
    private void maltsFromRegionAction(ActionEvent event) {
        txtMessage.setText("Search by region: under development");
    }

    @FXML
    private void maltsInAgeRangeAction(ActionEvent event) {
        txtMessage.setText("Search by age range: under development");
    }

    @FXML
    private void allMaltsAction(ActionEvent event) {
        int count = wdm.findAllMalts();
        WhiskeyData.WhiskeyDetails first = wdm.first();
        if (first != null) {
            display(first);
            txtMessage.setText(count + " records found.");
        } else {
            txtMessage.setText("No records found.");
        }
    }

    @FXML
    private void ClearAction(ActionEvent event) {
        txtDistillery.clear();
        txtAge.clear();
        txtRegion.clear();
        txtPrice.clear();
        txtMaltsFromRegion.clear();
        txtLowerAge.clear();
        txtUpperAge.clear();
        txtMessage.clear();
    }

    @FXML
    private void exitAction(ActionEvent event) {
        System.exit(0);
    }
    
    private void display(WhiskeyData.WhiskeyDetails d) {
        if (d != null) {
            txtDistillery.setText(d.distillery());
            txtAge.setText(String.valueOf(d.age()));
            txtRegion.setText(d.region());
            txtPrice.setText("$" + d.price());
        }
    }

}
