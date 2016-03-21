package eu.ortlepp.tasklist.gui;

import eu.ortlepp.tasklist.SimpleTaskList;
import eu.ortlepp.tasklist.extra.CustomSortedList;
import eu.ortlepp.tasklist.gui.components.DateTableCell;
import eu.ortlepp.tasklist.gui.components.DescriptionTableCell;
import eu.ortlepp.tasklist.gui.components.PriorityTableCell;
import eu.ortlepp.tasklist.logic.DueComperator;
import eu.ortlepp.tasklist.logic.PriorityComperator;
import eu.ortlepp.tasklist.logic.TaskController;
import eu.ortlepp.tasklist.model.ParentWindowData;
import eu.ortlepp.tasklist.model.Task;
import eu.ortlepp.tasklist.tools.ShortcutProperties;
import eu.ortlepp.tasklist.tools.UserProperties;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the main window. Handles all actions of the main window.
 *
 * @author Thorsten Ortlepp
 */
public class MainWindowController {

    /** A logger to write out events and messages to the console. */
    private static final Logger LOGGER = Logger.getLogger(MainWindowController.class.getName());


    /** Button to open a new task list. */
    @FXML
    private Button buttonOpen;


    /** Button to save a task list. */
    @FXML
    private Button buttonSave;


    /** Button to add a new task to the list. */
    @FXML
    private Button buttonNew;


    /** Button to add the currently selected task. */
    @FXML
    private Button buttonEdit;


    /** Button to mark the currently selected task as done. */
    @FXML
    private Button buttonDone;


    /** Button to delete the currently selected task from the list. */
    @FXML
    private Button buttonDelete;


    /** Button to move completed tasks to the archive. */
    @FXML
    private Button buttonMove;


    /** Button to open the settings dialog. */
    @FXML
    private Button buttonSettings;


    /** Button to open the info dialog. */
    @FXML
    private Button buttonInfo;


    /** CheckBox to filter done / not yet done tasks. */
    @FXML
    private CheckBox checkboxDone;


    /** ComboBox to filter which context(s) are shown. */
    @FXML
    private ComboBox<String> comboboxContext;


    /** ComboBox to filter which project(s) are shown. */
    @FXML
    private ComboBox<String> comboboxProject;


    /** Table to show the task list. */
    @FXML
    private TableView<Task> tableviewTasks;


    /** Column for the table to show the status (done / not yet done) of the tasks. */
    @FXML
    private TableColumn<Task, Boolean> tablecolumnStatus;


    /** Column for the table to show the priorities of the tasks. */
    @FXML
    private TableColumn<Task, String> tablecolumnPriority;


    /** Column for the table to show the due date of the tasks. */
    @FXML
    private TableColumn<Task, LocalDate> tablecolumnDue;


    /** Column for the table to show the descriptions of the tasks. */
    @FXML
    private TableColumn<Task, String> tablecolumnDescription;


    /** Column for the table to show the context of the tasks. */
    @FXML
    private TableColumn<Task, String> tablecolumnContext;


    /** Column for the table to show the project of the tasks. */
    @FXML
    private TableColumn<Task, String> tablecolumnProject;


    /** Label to show the currently opened file. */
    @FXML
    private Label labelFilename;


    /** Indicator for unsaved changes on the task list. */
    @FXML
    private Label labelSaved;


    /** Translated captions and tooltips for the GUI. */
    private final ResourceBundle translations;


    /** Controller for the loaded task list file. */
    private final TaskController tasks;


    /** The primary stage of the main window. */
    private Stage stage;


    /** The dialog window to add new tasks or edit existing tasks. */
    private DialogStage newEditDialog;


    /** The the about dialog. */
    private DialogStage aboutDialog;


    /** The controller of the new / edit dialog. */
    private NewEditDialogController newEditController;


    /** The dialog window to change settings. */
    private DialogStage settingsDialog;


    /** The controller of the settings dialog. */
    private SettingsDialogController settingsController;


    /** Status for the task list: Are there unsaved changes (false) or not (true). */
    private boolean saved;


    /** Listener for changes on done properties of tasks. */
    private CompletionListener completionListener;


    /**
     * Initialize controller by loading the translated captions and tooltips
     * for the GUI components.
     */
    public MainWindowController() {
        try {
            translations = ResourceBundle.getBundle(SimpleTaskList.TRANSLATION);
            tasks = new TaskController();
            initDialogs();
        } catch (MissingResourceException ex) {
            throw new RuntimeException("Translation is not available", ex);
        }
    }



    /**
     * Initialize the window. The icons are displayed on the buttons.
     */
    @FXML
    private void initialize() {
        /* Initialize buttons with icons and tooltips */
        initButton(buttonOpen, "open.png", "tooltip.button.open");
        initButton(buttonSave, "save.png", "tooltip.button.save");
        initButton(buttonNew, "new.png", "tooltip.button.new");
        initButton(buttonEdit, "edit.png", "tooltip.button.edit");
        initButton(buttonDone, "done.png", "tooltip.button.done");
        initButton(buttonDelete, "delete.png", "tooltip.button.delete");
        initButton(buttonMove, "move.png", "tooltip.button.move");
        initButton(buttonSettings, "settings.png", "tooltip.button.settings");
        initButton(buttonInfo, "info.png", "tooltip.button.info");

        /* Initialize context filter */
        tasks.getContextList().add(0, translations.getString("filter.context.all"));
        tasks.getContextList().add(1, translations.getString("filter.context.without"));
        comboboxContext.setItems(tasks.getContextList());
        comboboxContext.getSelectionModel().clearAndSelect(0);

        /* Initialize project filter */
        tasks.getProjectList().add(0, translations.getString("filter.project.all"));
        tasks.getProjectList().add(1, translations.getString("filter.project.without"));
        comboboxProject.setItems(tasks.getProjectList());
        comboboxProject.getSelectionModel().clearAndSelect(0);

        /* Custom renderers for nonstandard table cells */
        tablecolumnStatus.setCellFactory(CheckBoxTableCell.forTableColumn(tablecolumnStatus));
        tablecolumnPriority.setCellFactory(column -> new PriorityTableCell());
        tablecolumnDue.setCellFactory(column -> new DateTableCell());
        tablecolumnDescription.setCellFactory(column -> new DescriptionTableCell());

        /* Show data from the model in the table */
        tablecolumnStatus.setCellValueFactory(cellData -> cellData.getValue().doneProperty());
        tablecolumnPriority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        tablecolumnDue.setCellValueFactory(cellData -> cellData.getValue().dueProperty());
        tablecolumnDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        tablecolumnContext.setCellValueFactory(cellData -> cellData.getValue().contextStringProperty());
        tablecolumnProject.setCellValueFactory(cellData -> cellData.getValue().projectStringProperty());

        /* Custom comperators to achieve a correct sorting */
        tablecolumnPriority.setComparator(new PriorityComperator());
        tablecolumnDue.setComparator(new DueComperator());

        /* Wrap task list in filtered list to enable filtering */
        final FilteredList<Task> filteredTasks = new FilteredList<>(tasks.getTaskList(), p -> true);

        /* Listener to filter by done / not yet done */
        checkboxDone.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filteredTasks.setPredicate(task -> {
                return filterTableItem(task);
            });
        });

        /* Listener to filter by context */
        comboboxContext.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredTasks.setPredicate(task -> {
                return filterTableItem(task);
            });
        });

        /* Listener to filter by project */
        comboboxProject.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredTasks.setPredicate(task -> {
                return filterTableItem(task);
            });
        });

        /* Wrap filtered list in sorted list to enable sorting */
        final CustomSortedList<Task> sortedTasks = new CustomSortedList<>(filteredTasks);
        sortedTasks.comparatorProperty().bind(tableviewTasks.comparatorProperty());

        tableviewTasks.setItems(sortedTasks);

        /* Sort columns */
        tableviewTasks.getSortOrder().add(tablecolumnPriority);
        tableviewTasks.getSortOrder().add(tablecolumnDue);

        /* Resizing the table: expand columns to full width */
        tableviewTasks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setSaved(true);
        completionListener = new CompletionListener();
    }



    /**
     * Initialize dialogs by loading their FXML and getting access to their controllers.
     */
    private void initDialogs() {
        /* Initialize new / edit dialog */
        newEditDialog = new DialogStage();
        newEditController = (NewEditDialogController) initDialog(newEditDialog, "NewEditDialog.fxml");
        newEditController.setStage(newEditDialog);

        /* Initialize about dialog */
        aboutDialog = new DialogStage();
        final AboutDialogController aboutController =
                (AboutDialogController) initDialog(aboutDialog, "AboutDialog.fxml");
        aboutController.setStage(aboutDialog);
        aboutDialog.setTitle(translations.getString("about.title"));

        /* Initialize settings dialog */
        settingsDialog = new DialogStage();
        settingsController = (SettingsDialogController) initDialog(settingsDialog, "SettingsDialog.fxml");
        settingsController.setStage(settingsDialog);
        settingsDialog.setTitle(translations.getString("settings.title"));
    }



    /**
     * Initialize a dialog. Load its FXML file, set up the GUI and the controller.
     *
     * @param dialog The stage of the dialog
     * @param fxml The FXML file of the GUI
     * @return The initialized controller for the dialog
     */
    private AbstractDialogController initDialog(final DialogStage dialog, final String fxml) {
        AbstractDialogController controller = null;

        /* Load FXML */
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("eu/ortlepp/tasklist/fxml/" + fxml), translations);
            final Parent root = loader.load();
            dialog.setScene(new Scene(root));
            controller = loader.getController();
        } catch (IOException ex) {
            LOGGER.severe("Initialization of dialog failed: " + ex.getMessage());
        }

        /* Set properties */
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);

        /* Handler to place the dialog in the center of the main window instead of the screen center */
        dialog.setOnShown(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                dialog.setPosition(getCurrentWindowData());
            }
        });

        return controller;
    }



    /**
     * Initialize a button by removing its text and displaying an icon on it.
     *
     * @param button The button to be initialized
     * @param icon The file name of the icon to be displayed on the button
     * @param tooltip The key for the tooltip in the translation
     */
    private void initButton(final Button button, final String icon, final String tooltip) {
        button.setText("");
        final String iconfile = "eu/ortlepp/tasklist/icons/" + icon;
        final Image iconimage = new Image(getClass().getClassLoader().getResourceAsStream(iconfile));
        button.setGraphic(new ImageView(iconimage));
        if (UserProperties.getInstance().isShowTooltips()) {
            button.setTooltip(new Tooltip(translations.getString(tooltip)));
        }
    }



    /**
     * Initialize the keyboard shortcuts.
     */
    public void initShortcuts() {
        ShortcutProperties shortcutProp = ShortcutProperties.getInstance();

        /* Open file */
        addShortcut(shortcutProp.getKeyOpen(),
                new Runnable() {
                    @Override
                    public void run() {
                        handleFileOpen();
                    }
                 });

        /* Save file */
        addShortcut(shortcutProp.getKeySave(),
                new Runnable() {
                    @Override
                    public void run() {
                        handleFileSave();
                    }
                 });

        /* New task */
        addShortcut(shortcutProp.getKeyNew(),
                new Runnable() {
                    @Override
                    public void run() {
                        handleNewTask();
                    }
                 });

        /* Edit task */
        addShortcut(shortcutProp.getKeyEdit(),
                new Runnable() {
                    @Override
                    public void run() {
                        handleEditTask();
                    }
                 });

        /* Mark task as done */
        addShortcut(shortcutProp.getKeyDone(),
                new Runnable() {
                    @Override
                    public void run() {
                        handleTaskDone();
                    }
                 });

        /* Delete task */
        addShortcut(shortcutProp.getKeyDelete(),
                new Runnable() {
                    @Override
                    public void run() {
                        handleTaskDelete();
                    }
                 });

        /* Move completed tasks to archive */
        addShortcut(shortcutProp.getKeyMove(),
                new Runnable() {
                    @Override
                    public void run() {
                        handleMoveToArchive();
                    }
                 });
    }


    /**
     * Add a keyboard shortcut to the window / its stage and scene. The shortcut is created as a
     * combination of CTRL (or Meta) and the given key.
     *
     * @param key The key that is used to create the shortcut
     * @param action The action that is triggered by the shortcut
     */
    private void addShortcut(String key, Runnable action) {
        KeyCodeCombination keycode =
                new KeyCodeCombination(KeyCode.getKeyCode(key), KeyCombination.SHORTCUT_DOWN);
        stage.getScene().getAccelerators().put(keycode, action);
    }



    /**
     * Handle events when a key is pressed. Pressing DEL deletes the currently selected task,
     * pressing ENTER opens the editing dialog for the currently selected task. These events
     * are hard-coded to create a "natural" behavior of the table for the user.
     *
     * @param event Event that occurred
     */
    @FXML
    public void handleKeyEvents(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            handleTaskDelete();
        } else if (event.getCode() == KeyCode.ENTER) {
            handleEditTask();
        }
    }



    /**
     * Setter for the primary stage of the main window.
     *
     * @param stage The primary stage of the main window
     */
    public void setStage(final Stage stage) {
        this.stage = stage;
    }



    /**
     * Set the status for the task list: Are there unsaved changes (false) or not (true).
     *
     * @param saved Saved status: true = no changes, false = unsaved changes
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
        labelSaved.setVisible(!saved);
    }



    /**
     * Returns the status for the task list: Are there unsaved changes (false) or not (true).
     *
     * @return Saved status: true = no changes, false = unsaved changes
     */
    public boolean isSaved() {
        return saved;
    }



    /**
     * Handle to open a task list file: Show open dialog, load selected file.
     */
    @FXML
    private void handleFileOpen() {
        /* Initialize dialog */
        final FileChooser openDialog = new FileChooser();
        openDialog.setTitle(translations.getString("dialog.open.title"));
        openDialog.getExtensionFilters().addAll(
                new ExtensionFilter(translations.getString("dialog.open.filetype.text"), "*.txt"),
                new ExtensionFilter(translations.getString("dialog.open.filetype.all"), "*.*"));
        openDialog.setInitialDirectory(new File(System.getProperty("user.home")));

        /* Show dialog */
        final File file = openDialog.showOpenDialog(stage);

        /* Load selected file */
        if (file != null) {
            loadTaskList(file.getAbsolutePath());
        }
    }



    /**
     * Handle to save the currently open task list: Save task list to file.
     */
    @FXML
    public void handleFileSave() {
        if (!saved) {
            if (tasks.writeTaskList()) {
                setSaved(true);
            } else {
                final Alert message = new Alert(AlertType.ERROR);
                AbstractDialogController.prepareDialog(message, "dialog.write.title",
                        "dialog.write.header", "dialog.write.content", getCurrentWindowData());
                message.showAndWait();
            }
        }
    }



    /**
     * Handle to add a new task to the list: Open the new task dialog and
     * then add one or more tasks to the list.
     */
    @FXML
    private void handleNewTask() {
        /* Open new task dialog */
        newEditController.setNewDialog(tasks.getContextList(), tasks.getProjectList());
        newEditDialog.setTitle(translations.getString("dialog.new.title"));
        newEditDialog.showAndWait();

        /* If adding was not aborted */
        if (newEditController.isSaved()) {
            for (final Task task : newEditController.getNewTasks()) {
                /* Add task */
                Task newTask = new Task(task);
                tasks.getTaskList().add(newTask);

                /* Update context and project filters */
                for (final String item : task.getContext()) {
                    tasks.addContext(item);
                }
                for (final String item : task.getProject()) {
                    tasks.addProject(item);
                }

                addCompletionListener(newTask.doneProperty());
            }

            setSaved(false);
        }
    }



    /**
     * Handle to edit the currently selected task: Open edit dialog and
     * after editing update the edited task.
     */
    @FXML
    private void handleEditTask() {
        if (tableviewTasks.getSelectionModel().getSelectedIndex() != -1) {
            /* Open edit dialog */
            newEditController.setEditDialog(tableviewTasks.getSelectionModel().getSelectedItem(),
                    tasks.getContextList(), tasks.getProjectList());
            newEditDialog.setTitle(translations.getString("dialog.edit.title"));
            newEditDialog.showAndWait();

            /* If editing was not aborted */
            if (newEditController.isSaved()) {
                final Task temp = newEditController.getEditedTask();

                /* Update values */
                tableviewTasks.getSelectionModel().getSelectedItem().setDone(temp.isDone());
                tableviewTasks.getSelectionModel().getSelectedItem().setPriority(temp.getPriority());
                tableviewTasks.getSelectionModel().getSelectedItem().setCreation(temp.getCreation());
                tableviewTasks.getSelectionModel().getSelectedItem().setDue(temp.getDue());
                tableviewTasks.getSelectionModel().getSelectedItem().setDescription(temp.getDescription());

                /* Update contexts */
                tableviewTasks.getSelectionModel().getSelectedItem().clearContext();
                for (final String item : temp.getContext()) {
                    tableviewTasks.getSelectionModel().getSelectedItem().addToContext(item);
                    tasks.addContext(item);
                }

                /* Update projects */
                tableviewTasks.getSelectionModel().getSelectedItem().clearProject();
                for (final String item : temp.getProject()) {
                    tableviewTasks.getSelectionModel().getSelectedItem().addToProject(item);
                    tasks.addProject(item);
                }

                tableviewTasks.sort();
                setSaved(false);
            }
        }
    }



    /**
     * Handle to mark the currently selected task as done: If a task is selected mark
     * that task as done.
     */
    @FXML
    private void handleTaskDone() {
        if (tableviewTasks.getSelectionModel().getSelectedIndex() != -1) {
            tableviewTasks.getSelectionModel().getSelectedItem().setDone(true);
            setSaved(false);
            tableviewTasks.sort();
        }
    }



    /**
     * Handle to delete / remove the currently selected task from the list:
     * If a task is selected delete this task from the list.
     */
    @FXML
    private void handleTaskDelete() {
        if (tableviewTasks.getSelectionModel().getSelectedIndex() != -1) {

            /* Confirmation dialog */
            final Alert alert = new Alert(AlertType.CONFIRMATION);
            AbstractDialogController.prepareDialog(alert, "dialog.delete.title",
                    "dialog.delete.header", "dialog.delete.content", getCurrentWindowData());
            final Optional<ButtonType> choice = alert.showAndWait();

            /* Confirm deleting of the task */
            if (choice.get() == ButtonType.OK) {
                final long deleteTaskId =
                        tableviewTasks.getSelectionModel().getSelectedItem().getTaskId();
                int deleteListId = -1;

                /* Find the correct task */
                for (int i = 0; i < tasks.getTaskList().size(); i++) {
                    if (tasks.getTaskList().get(i).getTaskId() == deleteTaskId) {
                        deleteListId = i;
                        break;
                    }
                }

                /* Delete task from list */
                tasks.getTaskList().remove(deleteListId);

                setSaved(false);
            }
        }
    }



    /**
     * Handle to move all completed tasks into the archive: Add completed tasks to done.txt and
     * remove them from task list.
     */
    @FXML
    private void handleMoveToArchive() {
        final int moved = tasks.moveToArchive();

        if (moved > 0) {
            final Alert message = new Alert(AlertType.INFORMATION);
            AbstractDialogController.prepareDialog(message, "dialog.moved.title",
                    "dialog.moved.header", "dialog.moved.content", getCurrentWindowData());
            message.showAndWait();
            setSaved(false);
        }
    }



    /**
     * Handle to open the settings dialog: TBD.
     */
    @FXML
    private void handleOpenSettings() {
        settingsController.initShow();
        settingsDialog.showAndWait();
        if (settingsController.isSaved()) {
            final Alert message = new Alert(AlertType.INFORMATION);
            AbstractDialogController.prepareDialog(message, "dialog.settings.title",
                    "dialog.settings.header", "dialog.settings.content", getCurrentWindowData());
            message.showAndWait();
        }
    }



    /**
     * Handle to open the about / info dialog: show the info / about dialog.
     */
    @FXML
    private void handleOpenInfo() {
        aboutDialog.showAndWait();
    }



    /**
     * Load a task list from file. If the file name is null or an empty string no task list
     * will be loaded. An error message is shown when the task list could not be opened.
     *
     * @param file File name of the task list
     */
    public void loadTaskList(final String file) {
        if (file != null && !file.isEmpty() && new File(file).exists()) {

            if (tasks.loadTaskList(file)) {
                labelFilename.setText(tasks.getFilename());
                setSaved(true);

                /* Add listener for changes on done property */
                for (final Task task : tasks.getTaskList()) {
                    addCompletionListener(task.doneProperty());
                }

                /* Unlock GUI */
                buttonSave.setDisable(false);
                buttonNew.setDisable(false);
                buttonEdit.setDisable(false);
                buttonDone.setDisable(false);
                buttonDelete.setDisable(false);
                buttonMove.setDisable(false);
                checkboxDone.setDisable(false);
                comboboxContext.setDisable(false);
                comboboxProject.setDisable(false);
            } else {
                final Alert message = new Alert(AlertType.ERROR);
                AbstractDialogController.prepareDialog(message, "dialog.read.title",
                        "dialog.read.header", "dialog.read.content", getCurrentWindowData());
                message.showAndWait();
            }
        }
    }



    /**
     * Filter an item (= task) in the table and check if should be visible or hidden.
     *
     * @param item The item (= task) to check
     * @return Result of the check; true = show item in table, false = hide item
     */
    private boolean filterTableItem(final Task item) {
        boolean done = true;
        /* Only show done tasks when check box is checked */
        if (!checkboxDone.isSelected() && item.isDone()) {
            done = false;
        }

        final String selectedContext = comboboxContext.getSelectionModel().getSelectedItem();
        boolean context = false;

        /* Show task when "All Contexts" is selected or when "Without Context" is selected and
           task has no context or when task has context that matches selected context */
        if (selectedContext.equals(translations.getString("filter.context.all"))) {
            context = true;
        } else if (selectedContext.equals(translations.getString("filter.context.without"))
                && item.getContext().isEmpty()) {
            context = true;
        } else {
            for (final String itemContext : item.getContext()) {
                if (itemContext.equals(selectedContext)) {
                    context = true;
                    break;
                }
            }
        }

        final String selectedProject = comboboxProject.getSelectionModel().getSelectedItem();
        boolean project = false;

        /* Show task when "All Projects" is selected or when "Without Project" is selected
           and task has no project or when task has project that matches selected project */
        if (selectedProject.equals(translations.getString("filter.project.all"))) {
            project = true;
        } else if (selectedProject.equals(translations.getString("filter.project.without"))
                && item.getProject().isEmpty()) {
            project = true;
        } else {
            for (final String itemProject : item.getProject()) {
                if (itemProject.equals(selectedProject)) {
                    project = true;
                    break;
                }
            }
        }

        /* Combine the three filters and get final filter result */
        return done && context && project;
    }



    /**
     * Add a change listener to a boolean property to detect when a task completion status changes.
     *
     * @param property Boolean property
     */
    private void addCompletionListener(final BooleanProperty property) {
        property.addListener(completionListener);
    }



    /**
     * Create a ParentWindowData object with the current values from the window.
     *
     * @return The created ParentWindowData object
     */
    private ParentWindowData getCurrentWindowData() {
        return new ParentWindowData(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
    }



    ///// ----- Inner classes ----- \\\\\



    /**
     * Inner class: A change listener for the boolean done property. Changes the saved status
     * of the task list.
     *
     * @author Thorsten Ortlepp
     */
    private class CompletionListener implements ChangeListener<Boolean> {

        /**
         * Change event: Value is changing - set saved state to unsaved.
         *
         * @param observable The property
         * @param oldValue The old value of the property
         * @param newValue The new value of the property
         */
        @Override
        public void changed(final ObservableValue<? extends Boolean> observable,
                final Boolean oldValue, final Boolean newValue) {
            setSaved(false);
        }
    }

}
