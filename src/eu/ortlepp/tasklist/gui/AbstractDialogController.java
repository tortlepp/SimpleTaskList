package eu.ortlepp.tasklist.gui;

import java.util.ResourceBundle;

import eu.ortlepp.tasklist.SimpleTaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Abstract class with common functionality for all dialog windows.
 *
 * @author Thorsten Ortlepp
 */
public abstract class AbstractDialogController {

    /** The stage of the dialog. */
    protected Stage stage;


    /** Translated captions and tooltips for the GUI. */
    protected static ResourceBundle translations = ResourceBundle.getBundle(SimpleTaskList.TRANSLATION);


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



    /**
     * Initialize a dialog by setting its title, header and content
     * and set window style to "utility dialog".
     *
     * @param dialog The dialog to initialize
     * @param keyTitle Key for dialog title in the translation file
     * @param keyHeader Key for dialog header in the translation file
     * @param keyContent Key for dialog content in the translation file
     */
    protected static void prepareDialog(final Dialog dialog, final String keyTitle, final String keyHeader,
            final String keyContent) {
        dialog.setTitle(translations.getString(keyTitle));
        dialog.setHeaderText(translations.getString(keyHeader));
        dialog.setContentText(translations.getString(keyContent));
        ((Stage) dialog.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UTILITY);
    }

}
