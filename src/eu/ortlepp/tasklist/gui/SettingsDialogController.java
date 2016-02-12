package eu.ortlepp.tasklist.gui;

import java.io.File;

import eu.ortlepp.tasklist.tools.DefaultProperties;
import eu.ortlepp.tasklist.tools.ShortcutProperties;
import eu.ortlepp.tasklist.tools.UserProperties;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Controller for the settings dialog window. Handles all actions of the dialog window.
 *
 * @author Thorsten Ortlepp
 */
public class SettingsDialogController extends AbstractDialogController {

    /** TabPane to change between different settings groups. */
    @FXML
    private TabPane tabpaneTabs;


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


    /** Label to display the shortcut key. */
    @FXML
    private Label labelShortcut1;


    /** Label to display the shortcut key. */
    @FXML
    private Label labelShortcut2;


    /** Label to display the shortcut key. */
    @FXML
    private Label labelShortcut3;


    /** Label to display the shortcut key. */
    @FXML
    private Label labelShortcut4;


    /** Label to display the shortcut key. */
    @FXML
    private Label labelShortcut5;


    /** Label to display the shortcut key. */
    @FXML
    private Label labelShortcut6;


    /** Label to display the shortcut key. */
    @FXML
    private Label labelShortcut7;


    /** ComboBox to select the key for opening a file. */
    @FXML
    private ComboBox<String> comboboxKeyOpen;


    /** ComboBox to select the key for saving the task list. */
    @FXML
    private ComboBox<String> comboboxKeySave;


    /** ComboBox to select the key for creating a new task. */
    @FXML
    private ComboBox<String> comboboxKeyNew;


    /** ComboBox to select the key for editing an existing task. */
    @FXML
    private ComboBox<String> comboboxKeyEdit;


    /** ComboBox to select the key for marking the selected task as done. */
    @FXML
    private ComboBox<String> comboboxKeyDone;


    /** ComboBox to select the key for deleting the selected task. */
    @FXML
    private ComboBox<String> comboboxKeyDelete;


    /** ComboBox to select the key for moving completed task to the archive. */
    @FXML
    private ComboBox<String> comboboxKeyMove;


    /** Save status of the dialog: true = user clicked on "Save", false = user clicked on "Cancel". */
    private boolean saved;


    /** A list of all keys / letters. */
    private final String[] keys = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    /**
     * Initialize the dialog and its components.
     */
    @FXML
    @Override
    protected void initialize() {
        comboboxInterval.getItems().setAll(
                translations.getString("settings.preference.autosave.interval.values").split(";"));

        /* Get OS dependent shortcut key */
        final String shortcut;
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            shortcut = translations.getString("settings.shortcut.mac") + " +";
        } else {
            shortcut = translations.getString("settings.shortcut") + " +";
        }

        /* Show shortcut key in GUI */
        labelShortcut1.setText(shortcut);
        labelShortcut2.setText(shortcut);
        labelShortcut3.setText(shortcut);
        labelShortcut4.setText(shortcut);
        labelShortcut5.setText(shortcut);
        labelShortcut6.setText(shortcut);
        labelShortcut7.setText(shortcut);

        /* Fill ComboBoxey with values */
        comboboxKeyOpen.getItems().addAll(keys);
        comboboxKeySave.getItems().addAll(keys);
        comboboxKeyNew.getItems().addAll(keys);
        comboboxKeyEdit.getItems().addAll(keys);
        comboboxKeyDone.getItems().addAll(keys);
        comboboxKeyDelete.getItems().addAll(keys);
        comboboxKeyMove.getItems().addAll(keys);
    }



    /**
     * Prepare the window before it is shown to the user. Especially initialize all GUI components.
     */
    public void initShow() {
        saved = false;
        tabpaneTabs.getSelectionModel().select(0);

        /* Initialize GUI components with actual values */
        UserProperties userProp = UserProperties.getInstance();
        checkboxAutoSave.setSelected(userProp.isAutomaticSave());
        checkboxSaveOnClose.setSelected(userProp.isSaveOnClose());
        checkboxTooltips.setSelected(userProp.isShowTooltips());
        textfieldFile.setText(userProp.getStandardTasklist());
        textfieldArchive.setText(userProp.getArchiveFile());

        /* Translate minutes to selected item */
        int selectionIndex;
        switch (userProp.getAutomaticSaveInterval()) {
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

        /* Initialize ComboBoxes for shortcuts */
        ShortcutProperties shortcutProp = ShortcutProperties.getInstance();
        comboboxKeyOpen.getSelectionModel().select(getNumericValue(shortcutProp.getKeyOpen()));
        comboboxKeySave.getSelectionModel().select(getNumericValue(shortcutProp.getKeySave()));
        comboboxKeyNew.getSelectionModel().select(getNumericValue(shortcutProp.getKeyNew()));
        comboboxKeyEdit.getSelectionModel().select(getNumericValue(shortcutProp.getKeyEdit()));
        comboboxKeyDone.getSelectionModel().select(getNumericValue(shortcutProp.getKeyDone()));
        comboboxKeyDelete.getSelectionModel().select(getNumericValue(shortcutProp.getKeyDelete()));
        comboboxKeyMove.getSelectionModel().select(getNumericValue(shortcutProp.getKeyMove()));
    }



    /**
     * Converts a letter into a numeric value. The counting starts with 0.
     * For example A = 0, B = 1, C = 2, ...
     *
     * @param letter The letter to convert (only the first letter of the sting is converted)
     * @return The numeric value of the letter
     */
    private int getNumericValue(String letter) {
        return letter.charAt(0) - 'A';
    }



    /**
     * Handle a click on the open button: select a standard task list.
     */
    @FXML
    private void handleSelectFile() {
        /* Initialize dialog */
        final FileChooser openDialog = new FileChooser();
        openDialog.setTitle(translations.getString("dialog.open.title"));
        openDialog.getExtensionFilters().addAll(
                new ExtensionFilter(translations.getString("dialog.open.filetype.text"), "*.txt"),
                new ExtensionFilter(translations.getString("dialog.open.filetype.all"), "*.*"));
        openDialog.setInitialDirectory(new File(System.getProperty("user.home")));

        /* Show dialog */
        final File file = openDialog.showOpenDialog(stage);

        /* Show selected file in TextField */
        if (file != null) {
            textfieldFile.setText(file.getAbsolutePath());
        }
    }



    /**
     * Handle a click on the save button: save settings to preferences.
     */
    @FXML
    private void handleSave() {
        stage.hide();

        /* Save settings to preferences */
        UserProperties userProp = UserProperties.getInstance();
        userProp.updateAutomaticSave(checkboxAutoSave.isSelected());
        userProp.updateSaveOnClose(checkboxSaveOnClose.isSelected());
        userProp.updateShowTooltips(checkboxTooltips.isSelected());
        userProp.updateStandardTasklist(textfieldFile.getText());
        userProp.updateArchiveFile(textfieldArchive.getText());

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
        userProp.updateAutomaticSaveInterval(minutes);

        /* Save shortcuts to preferences */
        ShortcutProperties shortcutProp = ShortcutProperties.getInstance();
        shortcutProp.updateKeyOpen(getSelectedLetter(comboboxKeyOpen));
        shortcutProp.updateKeySave(getSelectedLetter(comboboxKeySave));
        shortcutProp.updateKeyNew(getSelectedLetter(comboboxKeyNew));
        shortcutProp.updateKeyEdit(getSelectedLetter(comboboxKeyEdit));
        shortcutProp.updateKeyDone(getSelectedLetter(comboboxKeyDone));
        shortcutProp.updateKeyDelete(getSelectedLetter(comboboxKeyDelete));
        shortcutProp.updateKeyMove(getSelectedLetter(comboboxKeyMove));

        saved = true;
    }



    /**
     * Reset all properties and shortcuts to their defaults.
     */
    @FXML
    private void handleRestore() {
        stage.hide();

        /* Save default settings to preferences */
        UserProperties userProp = UserProperties.getInstance();
        userProp.updateAutomaticSave(DefaultProperties.AUTOMATIC_SAVE);
        userProp.updateSaveOnClose(DefaultProperties.SAVE_ON_CLOSE);
        userProp.updateShowTooltips(DefaultProperties.SHOW_TOOLTIPS);
        userProp.updateStandardTasklist(DefaultProperties.STANDARD_TASKLIST);
        userProp.updateArchiveFile(DefaultProperties.ARCHIVE_FILE);
        userProp.updateAutomaticSaveInterval(DefaultProperties.AUTOMATIC_SAVE_INTERVAL);

        /* Save default shortcuts to preferences */
        ShortcutProperties shortcutProp = ShortcutProperties.getInstance();
        shortcutProp.updateKeyOpen(DefaultProperties.SHORTCUT_KEY_OPEN);
        shortcutProp.updateKeySave(DefaultProperties.SHORTCUT_KEY_SAVE);
        shortcutProp.updateKeyNew(DefaultProperties.SHORTCUT_KEY_NEW);
        shortcutProp.updateKeyEdit(DefaultProperties.SHORTCUT_KEY_EDIT);
        shortcutProp.updateKeyDone(DefaultProperties.SHORTCUT_KEY_DONE);
        shortcutProp.updateKeyDelete(DefaultProperties.SHORTCUT_KEY_DELETE);
        shortcutProp.updateKeyMove(DefaultProperties.SHORTCUT_KEY_MOVE);

        saved = true;
    }



    /**
     * Get the currently selected letter from a ComboBox.
     *
     * @param combobox The ComboBox (source)
     * @return The currently selected letter
     */
    private String getSelectedLetter(ComboBox<String> combobox) {
        return combobox.getItems().get(combobox.getSelectionModel().getSelectedIndex());
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
