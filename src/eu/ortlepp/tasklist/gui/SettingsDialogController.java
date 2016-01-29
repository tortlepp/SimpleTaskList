package eu.ortlepp.tasklist.gui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Controller for the settings dialog window. Handles all actions of the dialog window.
 *
 * @author Thorsten Ortlepp
 */
public class SettingsDialogController extends AbstractDialogController {

    /** CheckBox for automatically saving the task list. */
    @FXML
    private CheckBox checkboxAutoSave;


    /** CheckBox for saving the task list when the program is closed. */
    @FXML
    private CheckBox checkboxSaveOnClose;


    /** CheckBox to either show or hide tooltips in the main window. */
    @FXML
    private CheckBox checkboxTooltips;


    /** ComboBox to select the saving interval for automatic saves. */
    @FXML
    private ComboBox<String> comboboxInterval;


    /** Input to show the name of the standard task list file. */
    @FXML
    private TextField textfieldFile;


    /** Input to set the archive file name. */
    @FXML
    private TextField textfieldArchive;


    /** Save status of the dialog: true = user clicked on "Save", false = user clicked on "Cancel". */
    private boolean saved;


    /**
     * Initialize the dialog and its components.
     */
    @Override
    protected void initialize() {
        // TODO ...
    }



    /**
     * Initialize the ComboBox by setting its values.
     *
     * @param values Values to show in the ComboBox
     */
    public void initComboBoxInterval(String[] values) {
        comboboxInterval.getItems().setAll(values);
    }



    /**
     * Prepare the window before it is shown to the user.
     */
    public void initShow() {
        saved = false;
    }



    /**
     * Handle a click on the save button: save settings to preferences.
     */
    @FXML
    protected void handleSave() {
        // TODO ...
        stage.hide();
        saved = true;
    }



    /**
     * Getter for the save status of the dialog.
     *
     * @return The save status: true = user clicked on "Save", false = user clicked on "Cancel".
     */
    public boolean isSaved() {
        return saved;
    }

}
