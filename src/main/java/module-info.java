/**
 * Module definition for the CQU Whiskey Information System (WIS) application.
 * 
 * <p>This module utilizes JavaFX for building the user interface and Java SQL for database operations.</p>
 * 
 * <ul>
 *   <li>JavaFX modules are required for UI controls and FXML handling.</li>
 *   <li>java.base is included by default, while java.sql is explicitly declared for database access.</li>
 *   <li>The 'opens' directives allow reflective access to specific packages needed by FXML loaders.</li>
 * </ul>
 * 
 * @author Prajita Bhandari
 * 
 */
module cqu.wis {
    /** 
     * Requires JavaFX Controls module to build UI components like buttons, tables, etc.
     */
    requires javafx.controls;

    /** 
     * Requires JavaFX FXML module to support loading and interacting with FXML files.
     */
    requires javafx.fxml;

    /**
     * Requires base Java module (included by default) and the SQL module for JDBC database operations.
     */
    requires java.base;
    requires java.sql;

    /**
     * Opens the 'cqu.wis' package to javafx.fxml to allow reflective access during FXML loading.
     */
    opens cqu.wis to javafx.fxml;

    /**
     * Exports the 'cqu.wis' package so it can be accessed by other modules.
     */
    exports cqu.wis;

    /**
     * Opens the 'cqu.wis.view' package to javafx.fxml to enable FXML loader to access controllers.
     */
    opens cqu.wis.view to javafx.fxml;

    /**
     * Exports the 'cqu.wis.view' package to make UI-related classes available externally.
     */
    exports cqu.wis.view;
}
