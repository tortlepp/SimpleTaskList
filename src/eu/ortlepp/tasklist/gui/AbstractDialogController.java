package eu.ortlepp.tasklist.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Abstract class with common functionality for all dialog windows.
 *
 * @author Thorsten Ortlepp
 */
public abstract class AbstractDialogController {

    /** The stage of the dialog. */
    protected Stage stage;


    /**
     * Setter for the stage of the dialog window.
     *
     * @param stage The stage of the dialog window.
     */
    public void setStage(final Stage stage) {
        this.stage = stage;
    }



    /**
     * Handle a click on the cancel / close button: hide / close the dialog window.
     */
    @FXML
    protected void handleDialogHide() {
        stage.hide();
    }



    /**
     * Initialize the dialog and its components. Implementation is done the child classes.
     */
    protected abstract void initialize();

}
