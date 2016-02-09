package eu.ortlepp.tasklist.gui;

import eu.ortlepp.tasklist.tools.UserProperties;
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
     * Prepare the window before it is shown to the user. Especially initialize all GUI components.
     */
    public void initShow() {
        saved = false;

        /* Initialize GUI components with actual values */
        UserProperties properties = UserProperties.getInstance();
        checkboxAutoSave.setSelected(properties.isAutomaticSave());
        checkboxSaveOnClose.setSelected(properties.isSaveOnClose());
        checkboxTooltips.setSelected(properties.isShowTooltips());
        textfieldFile.setText(properties.getStandardTasklist());
        textfieldArchive.setText(properties.getArchiveFile());

        /* Translate minutes to selected item */
        int selectionIndex;
        switch (properties.getAutomaticSaveInterval()) {
            case 3:
                selectionIndex = 0;
                break;
            case 5:
                selectionIndex = 1;
                break;
            case 10:
                selectionIndex = 2;
                break;
            default:
                selectionIndex = 3;
                break;
        }
        comboboxInterval.getSelectionModel().select(selectionIndex);
    }



    /**
     * Handle a click on the save button: save settings to preferences.
     */
    @FXML
    protected void handleSave() {
        stage.hide();

        /* Save values to preferences */
        UserProperties properties = UserProperties.getInstance();
        properties.updateAutomaticSave(checkboxAutoSave.isSelected());
        properties.updateSaveOnClose(checkboxSaveOnClose.isSelected());
        properties.updateShowTooltips(checkboxTooltips.isSelected());
        properties.updateStandardTasklist(textfieldFile.getText());
        properties.updateArchiveFile(textfieldArchive.getText());

        /* Translate selected item to minutes */
        int minutes;
        switch (comboboxInterval.getSelectionModel().getSelectedIndex()) {
            case 0:
                minutes = 3;
                break;
            case 1:
                minutes = 5;
                break;
            case 2:
                minutes = 10;
                break;
            default:
                minutes = 15;
                break;
        }
        properties.updateAutomaticSaveInterval(minutes);

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
