package eu.ortlepp.tasklist.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import eu.ortlepp.tasklist.SimpleTaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller for the main window.
 */
public class MainWindowController {


    /** Button to open a new task list. */
    @FXML
    private Button btnOpen;


    /** Button to add a new task to the list. */
    @FXML
    private Button btnNew;


    /** Button to add the currently selected task. */
    @FXML
    private Button btnEdit;


    /** Button to mark the currently selected task as done. */
    @FXML
    private Button btnDone;


    /** Button to delete the currently selected task from the list. */
    @FXML
    private Button btnDelete;


    /** Button to open the settings dialog. */
    @FXML
    private Button btnSettings;


    /** Button to open the info dialog. */
    @FXML
    private Button btnInfo;


    /** Translated captions and tooltips for the GUI. */
    private final ResourceBundle translations;


    /**
     * Initialize controller by loading the translated captions and tooltips for the GUI components.
     */
    public MainWindowController() {
        try {
            translations = ResourceBundle.getBundle(SimpleTaskList.TRANSLATION);
        } catch (MissingResourceException ex) {
            throw new RuntimeException("Translation is not available", ex);
        }
    }



    /**
     * Initialize the window. The icons are displayed on the buttons.
     */
    @FXML
    private void initialize() {
        initButton(btnOpen, "open.png", "tooltip.button.open");
        initButton(btnNew, "new.png", "tooltip.button.new");
        initButton(btnEdit, "edit.png", "tooltip.button.edit");
        initButton(btnDone, "done.png", "tooltip.button.done");
        initButton(btnDelete, "delete.png", "tooltip.button.delete");
        initButton(btnSettings, "settings.png", "tooltip.button.settings");
        initButton(btnInfo, "info.png", "tooltip.button.info");
    }



    /**
     * Initialize a button by removing its text and displaying an icon on it.
     *
     * @param button The button to be initialized
     * @param icon The file name of the icon to be displayed on the button
     * @param tooltip The key for the tooltip in the translation
     */
    private void initButton(Button button, String icon, String tooltip) {
        button.setText("");
        String iconfile = "eu/ortlepp/tasklist/icons/" + icon;
        Image iconimage = new Image(getClass().getClassLoader().getResourceAsStream(iconfile));
        button.setGraphic(new ImageView(iconimage));
        button.setTooltip(new Tooltip(translations.getString(tooltip)));
    }



    /**
     * Handle a click on the "open" button: TBD.
     */
    @FXML
    private void handleBtnOpenClick() {
        System.out.println("OPEN");
    }



    /**
     * Handle a click on the "new" button: TBD.
     */
    @FXML
    private void handleBtnNewClick() {
        System.out.println("NEW");
    }



    /**
     * Handle a click on the "edit" button: TBD.
     */
    @FXML
    private void handleBtnEditClick() {
        System.out.println("EDIT");
    }



    /**
     * Handle a click on the "done" button: TBD.
     */
    @FXML
    private void handleBtnDoneClick() {
        System.out.println("DONE");
    }



    /**
     * Handle a click on the "delete" button: TBD.
     */
    @FXML
    private void handleBtnDeleteClick() {
        System.out.println("DELETE");
    }



    /**
     * Handle a click on the "settings" button: TBD.
     */
    @FXML
    private void handleBtnSettingsClick() {
        System.out.println("SETTINGS");
    }



    /**
     * Handle a click on the "info" button: TBD.
     */
    @FXML
    private void handleBtnInfoClick() {
        System.out.println("INFO");
    }

}
