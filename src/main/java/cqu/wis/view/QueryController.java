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
 * Controller for the whiskey-query interface.
 * <p>
 * Provides functionality for navigating through whiskey records, filtering by
 * region and age range, displaying all available malts, and handling form
 * validation.
 * </p>
 *
 * @author Prajita Bhandari
 *
 */
public class QueryController implements Initializable {

    /**
     * Text field for displaying and entering distillery name.
     */
    @FXML
    private TextField txtDistillery;

    /**
     * Text field for displaying and entering whiskey age.
     */
    @FXML
    private TextField txtAge;

    /**
     * Text field for displaying and entering whiskey region.
     */
    @FXML
    private TextField txtRegion;

    /**
     * Text field for displaying and entering whiskey price.
     */
    @FXML
    private TextField txtPrice;

    /**
     * Text area for displaying status messages, validation errors, and query
     * results.
     */
    @FXML
    private TextArea txtMessage;

    /**
     * Button for navigating to the next whiskey record.
     */
    @FXML
    private Button btnNext;

    /**
     * Button for navigating to the previous whiskey record.
     */
    @FXML
    private Button btnPrevious;

    /**
     * Button for executing malts from region query.
     */
    @FXML
    private Button btnMaltsFromRegion;

    /**
     * Button for executing malts in age range query.
     */
    @FXML
    private Button btnMaltsInAgeRange;

    /**
     * Text field for entering region name for region-based queries.
     */
    @FXML
    private TextField txtMaltsFromRegion;

    /**
     * Text field for entering lower bound of age range.
     */
    @FXML
    private TextField txtLowerAge;

    /**
     * Text field for entering upper bound of age range.
     */
    @FXML
    private TextField txtUpperAge;

    /**
     * Button for displaying all available whiskey malts.
     */
    @FXML
    private Button btnAllMalts;

    /**
     * Button for clearing all form fields.
     */
    @FXML
    private Button btnClear;

    /**
     * Button for exiting the application.
     */
    @FXML
    private Button btnExit;

    /**
     * SceneCoordinator for switching between different scenes.
     */
    private SceneCoordinator sc;

    /**
     * WhiskeyDataManager for accessing and navigating whiskey records.
     */
    private WhiskeyDataManager wdm;

    /**
     * WhiskeyDataValidator for validating region and age input fields.
     */
    private WhiskeyDataValidator wdv;

    /**
     * Injects required dependencies into the controller.
     *
     * @param sc The {@link SceneCoordinator} instance for managing scene
     * transitions.
     * @param wdm The {@link WhiskeyDataManager} instance for data operations
     * and queries.
     * @param wdv The {@link WhiskeyDataValidator} instance for input
     * validation.
     */
    public void inject(SceneCoordinator sc, WhiskeyDataManager wdm, WhiskeyDataValidator wdv) {
        this.sc = sc;
        this.wdm = wdm;
        this.wdv = wdv;
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
     * Handles the next button action to navigate to the next whiskey record.
     * Does not wrap around; if on the last record, shows a message.
     *
     * @param event The {@link ActionEvent} triggered by clicking the next
     * button.
     */
    @FXML
    private void nextAction(ActionEvent event) {
        var next = wdm.next();
        if (next != null) {
            display(next);
            txtMessage.clear();
        } else {
            txtMessage.setText("You are at the last record.");
        }
    }

    /**
     * Handles the previous button action to navigate to the previous whiskey
     * record. Does not wrap around; if on the first record, shows a message.
     *
     * @param event The {@link ActionEvent} triggered by clicking the previous
     * button.
     */
    @FXML
    private void previousAction(ActionEvent event) {
        var prev = wdm.previous();
        if (prev != null) {
            display(prev);
            txtMessage.clear();
        } else {
            txtMessage.setText("You are at the first record.");
        }
    }

    /**
     * Handles the malts from region query action.
     *
     * @param event The {@link ActionEvent} triggered by clicking the malts from
     * region button.
     */
    @FXML
    private void maltsFromRegionAction(ActionEvent event) {
        String region = txtMaltsFromRegion.getText();

        // Validate the region input
        var check = wdv.checkRegion(region);
        if (!check.valid()) {
            txtMessage.setText(check.message());
            clearDisplayFields();
            return;
        }

        // Execute query and handle results
        var results = wdm.getMaltsFromRegion(region);
        if (results.isEmpty()) {
            txtMessage.setText("No records found.");
            clearDisplayFields();
        } else {
            wdm.setDetails(results.toArray(new WhiskeyData.WhiskeyDetails[0]));
            var current = wdm.getCurrent();
            if (current != null) {
                display(current);
                txtMessage.setText(results.size() + " records found.");
            }
        }
    }

    /**
     * Handles the "Malts in Age Range" button action.
     * <p>
     * This method validates user inputs from the lower and upper age text
     * fields and executes a query for malt whiskey records that fall within the
     * specified age range.
     * </p>
     *
     * @param event The {@link javafx.event.ActionEvent} triggered by clicking
     * the button.
     */
    @FXML
    private void maltsInAgeRangeAction(ActionEvent event) {
        String leftText = txtLowerAge.getText().trim();
        String rightText = txtUpperAge.getText().trim();

        // Check if both fields are empty
        if (leftText.isEmpty() && rightText.isEmpty()) {
            txtMessage.setText("Error: Please enter at least one age value.");
            clearDisplayFields();
            return;
        }

        int left, right;

        try {
            // Both values given
            if (!leftText.isEmpty() && !rightText.isEmpty()) {
                left = Integer.parseInt(leftText);
                right = Integer.parseInt(rightText);
            } // Only left given
            else if (!leftText.isEmpty()) {
                left = Integer.parseInt(leftText);
                right = 100;
            } // Only right given
            else {
                right = Integer.parseInt(rightText);
                left = 0;
            }

            // Validate non-negative values
            if (left < 0 || right < 0) {
                txtMessage.setText("Error: Age values must not be negative.");
                clearDisplayFields();
                return;
            }

            // Validate logical order
            if (right < left) {
                txtMessage.setText("Error: Upper bound cannot be less than lower bound.");
                clearDisplayFields();
                return;
            }

            if (left == right) {
                txtMessage.setText("Error: Lower and upper bounds must be different.");
                clearDisplayFields();
                return;
            }

            // Valid range, perform query
            var results = wdm.getMaltsInAgeRange(left, right);
            if (results.isEmpty()) {
                txtMessage.setText("No records found.");
                clearDisplayFields();
            } else {
                wdm.setDetails(results.toArray(new WhiskeyData.WhiskeyDetails[0]));
                var current = wdm.getCurrent();
                if (current != null) {
                    display(current);
                    txtMessage.setText(results.size() + " records found.");
                }
            }
        } catch (NumberFormatException e) {
            txtMessage.setText("Error: Please enter valid whole numbers.");
            clearDisplayFields();
        }
    }

    /**
     * Handles the all malts query action.
     *
     * @param event The {@link ActionEvent} triggered by clicking the all malts
     * button.
     */
    @FXML
    private void allMaltsAction(ActionEvent event) {
        int count = wdm.findAllMalts();
        var current = wdm.getCurrent();
        if (current != null) {
            display(current);
            txtMessage.setText(count + " records found.");
        } else {
            txtMessage.setText("No records found.");
            clearDisplayFields();
        }
    }

    /**
     * Handles the clear button action to reset all form fields.
     *
     * @param event The {@link ActionEvent} triggered by clicking the clear
     * button.
     */
    @FXML
    private void ClearAction(ActionEvent event) {
        clearDisplayFields();
        txtMaltsFromRegion.clear();
        txtLowerAge.clear();
        txtUpperAge.clear();
        txtMessage.clear();
    }

    /**
     * Handles the exit button action to terminate the application.
     *
     * @param event The {@link ActionEvent} triggered by clicking the exit
     * button.
     */
    @FXML
    private void exitAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Displays whiskey details in the form fields.
     *
     * @param d The {@link WhiskeyData.WhiskeyDetails} object containing whiskey
     * information to display. If {@code null}, the fields remain unchanged.
     */
    private void display(WhiskeyData.WhiskeyDetails d) {
        if (d != null) {
            txtDistillery.setText(d.distillery());
            txtAge.setText(String.valueOf(d.age()));
            txtRegion.setText(d.region());
            txtPrice.setText("$" + d.price());
        }
    }

    /**
     * Clears all four display text fields (distillery, age, region, price).
     */
    private void clearDisplayFields() {
        txtDistillery.clear();
        txtAge.clear();
        txtRegion.clear();
        txtPrice.clear();
    }
}
