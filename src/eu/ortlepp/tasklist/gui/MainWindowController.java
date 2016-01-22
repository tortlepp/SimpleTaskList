package eu.ortlepp.tasklist.gui;

import eu.ortlepp.tasklist.SimpleTaskList;
import eu.ortlepp.tasklist.gui.components.DateTableCell;
import eu.ortlepp.tasklist.gui.components.DescriptionTableCell;
import eu.ortlepp.tasklist.gui.components.PriorityTableCell;
import eu.ortlepp.tasklist.logic.DueComperator;
import eu.ortlepp.tasklist.logic.PriorityComperator;
import eu.ortlepp.tasklist.logic.TaskController;
import eu.ortlepp.tasklist.model.Task;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Button btnOpen;


    /** Button to save a task list. */
    @FXML
    private Button btnSave;


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


    /** CheckBox to filter done / not yet done tasks. */
    @FXML
    private CheckBox chkbxDone;


    /** ComboBox to filter which context(s) are shown. */
    @FXML
    private ComboBox<String> cbxContext;


    /** ComboBox to filter which project(s) are shown. */
    @FXML
    private ComboBox<String> cbxProject;


    /** Table to show the task list. */
    @FXML
    private TableView<Task> tableTasks;


    /** Column for the table to show the status (done / not yet done) of the tasks. */
    @FXML
    private TableColumn<Task, Boolean> columnStatus;


    /** Column for the table to show the priorities of the tasks. */
    @FXML
    private TableColumn<Task, String> columnPriority;


    /** Column for the table to show the due date of the tasks. */
    @FXML
    private TableColumn<Task, LocalDate> columnDue;


    /** Column for the table to show the descriptions of the tasks. */
    @FXML
    private TableColumn<Task, String> columnDescription;


    /** Column for the table to show the context of the tasks. */
    @FXML
    private TableColumn<Task, String> columnContext;


    /** Column for the table to show the project of the tasks. */
    @FXML
    private TableColumn<Task, String> columnProject;


    /** Translated captions and tooltips for the GUI. */
    private final ResourceBundle translations;


    /** Controller for the loaded task list file. */
    private final TaskController tasks;


    /** The primary stage of the main window. */
    private Stage stage;


    /** The dialog window to add new tasks or edit existing tasks. */
    private Stage newEditDialog;


    /** The the about dialog. */
    private Stage aboutDialog;


    /** The controller of the new / edit dialog. */
    private NewEditDialogController newEditController;


    /** The controller of the about dialog. */
    private AboutDialogController aboutController;


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
        initButton(btnOpen, "open.png", "tooltip.button.open");
        initButton(btnSave, "save.png", "tooltip.button.save");
        initButton(btnNew, "new.png", "tooltip.button.new");
        initButton(btnEdit, "edit.png", "tooltip.button.edit");
        initButton(btnDone, "done.png", "tooltip.button.done");
        initButton(btnDelete, "delete.png", "tooltip.button.delete");
        initButton(btnSettings, "settings.png", "tooltip.button.settings");
        initButton(btnInfo, "info.png", "tooltip.button.info");

        /* Initialize context filter */
        tasks.getContextList().add(0, translations.getString("filter.context.all"));
        tasks.getContextList().add(1, translations.getString("filter.context.without"));
        cbxContext.setItems(tasks.getContextList());
        cbxContext.getSelectionModel().clearAndSelect(0);

        /* Initialize project filter */
        tasks.getProjectList().add(0, translations.getString("filter.project.all"));
        tasks.getProjectList().add(1, translations.getString("filter.project.without"));
        cbxProject.setItems(tasks.getProjectList());
        cbxProject.getSelectionModel().clearAndSelect(0);

        /* Custom renderers for nonstandard table cells */
        columnStatus.setCellFactory(CheckBoxTableCell.forTableColumn(columnStatus));
        columnPriority.setCellFactory(column -> new PriorityTableCell());
        columnDue.setCellFactory(column -> new DateTableCell());
        columnDescription.setCellFactory(column -> new DescriptionTableCell());

        /* Show data from the model in the table */
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().doneProperty());
        columnPriority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        columnDue.setCellValueFactory(cellData -> cellData.getValue().dueProperty());
        columnDescription
                .setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        columnContext.setCellValueFactory(cellData -> cellData.getValue().contextStringProperty());
        columnProject.setCellValueFactory(cellData -> cellData.getValue().projectStringProperty());

        /* Custom comperators to achieve a correct sorting */
        columnPriority.setComparator(new PriorityComperator());
        columnDue.setComparator(new DueComperator());

        /* Wrap task list in filtered list to enable filtering */
        FilteredList<Task> filteredTasks = new FilteredList<>(tasks.getTaskList(), p -> true);

        /* Listener to filter by done / not yet done */
        chkbxDone.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filteredTasks.setPredicate(task -> {
                return filterTableItem(task);
            });
        });

        /* Listener to filter by context */
        cbxContext.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredTasks.setPredicate(task -> {
                return filterTableItem(task);
            });
        });

        /* Listener to filter by project */
        cbxProject.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredTasks.setPredicate(task -> {
                return filterTableItem(task);
            });
        });

        /* Wrap filtered list in sorted list to enable sorting */
        SortedList<Task> sortedTasks = new SortedList<>(filteredTasks);
        sortedTasks.comparatorProperty().bind(tableTasks.comparatorProperty());

        tableTasks.setItems(sortedTasks);

        /* Sort columns */
        tableTasks.getSortOrder().add(columnPriority);
        tableTasks.getSortOrder().add(columnDue);

        /* Resizing the table: expand columns to full width */
        tableTasks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }



    /**
     * INitialize dialogs by loading their FXML and getting access to their controllers.
     */
    private void initDialogs() {
        /* Initialize new / edit dialog */
        newEditDialog = new Stage();
        newEditController = (NewEditDialogController) initDialog(newEditDialog,
                "NewEditDialog.fxml");
        newEditController.setStage(newEditDialog);

        /* Initialize about dialog */
        aboutDialog = new Stage();
        aboutController = (AboutDialogController) initDialog(aboutDialog, "AboutDialog.fxml");
        aboutController.setStage(aboutDialog);
        aboutDialog.setTitle(translations.getString("about.title"));
    }



    /**
     * Initialize a dialog. Load its FXML file, set up the GUI and the controller.
     *
     * @param dialog The stage of the dialog
     * @param fxml The FXML file of the GUI
     * @return The initialized controller for the dialog
     */
    private AbstractDialogController initDialog(Stage dialog, final String fxml) {
        AbstractDialogController controller = null;

        /* Load FXML */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("eu/ortlepp/tasklist/fxml/" + fxml), translations);
            Parent root = loader.load();
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
        String iconfile = "eu/ortlepp/tasklist/icons/" + icon;
        Image iconimage = new Image(getClass().getClassLoader().getResourceAsStream(iconfile));
        button.setGraphic(new ImageView(iconimage));
        button.setTooltip(new Tooltip(translations.getString(tooltip)));
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
     * Handle a click on the "open" button: Show open dialog, load selected file.
     */
    @FXML
    private void handleBtnOpenClick() {
        /* Initialize dialog */
        FileChooser openDialog = new FileChooser();
        openDialog.setTitle(translations.getString("dialog.open.title"));
        openDialog.getExtensionFilters().addAll(
                new ExtensionFilter(translations.getString("dialog.open.filetype.text"), "*.txt"),
                new ExtensionFilter(translations.getString("dialog.open.filetype.all"), "*.*"));

        /* Show dialog */
        File file = openDialog.showOpenDialog(stage);

        /* Load selected file */
        if (file != null && file.exists()) {
            loadTaskList(file.getAbsolutePath());
        }
    }



    /**
     * Handle a click on the "save" button: Save task list to file.
     */
    @FXML
    private void handleBtnSaveClick() {
        if (!tasks.writeTaskList()) {
            Alert message = new Alert(AlertType.ERROR);
            newEditController.initDialog(message, "dialog.write.title", "dialog.write.header",
                    "dialog.write.content");
            message.showAndWait();
        }
    }



    /**
     * Handle a click on the "new" button: Open the new task dialog and
     * then add one or more tasks to the list.
     */
    @FXML
    private void handleBtnNewClick() {
        /* Open new task dialog */
        newEditController.setNewDialog(tasks.getContextList(), tasks.getProjectList());
        newEditDialog.setTitle(translations.getString("dialog.new.title"));
        newEditDialog.showAndWait();

        /* If adding was not aborted */
        if (newEditController.isSaved()) {
            for (Task task : newEditController.getNewTasks()) {
                /* Add task */
                tasks.getTaskList().add(new Task(task));

                /* Update context and project filters */
                for (String item : task.getContext()) {
                    tasks.addContext(item);
                }
                for (String item : task.getProject()) {
                    tasks.addProject(item);
                }
            }
        }
    }



    /**
     * Handle a click on the "edit" button: Open edit dialog and
     * after editing update the edited task.
     */
    @FXML
    private void handleBtnEditClick() {
        if (tableTasks.getSelectionModel().getSelectedIndex() != -1) {
            /* Open edit dialog */
            newEditController.setEditDialog(tableTasks.getSelectionModel().getSelectedItem(),
                    tasks.getContextList(), tasks.getProjectList());
            newEditDialog.setTitle(translations.getString("dialog.edit.title"));
            newEditDialog.showAndWait();

            /* If editing was not aborted */
            if (newEditController.isSaved()) {
                Task temp = newEditController.getEditedTask();

                /* Update values */
                tableTasks.getSelectionModel().getSelectedItem().setDone(temp.isDone());
                tableTasks.getSelectionModel().getSelectedItem().setPriority(temp.getPriority());
                tableTasks.getSelectionModel().getSelectedItem().setCreation(temp.getCreation());
                tableTasks.getSelectionModel().getSelectedItem().setDue(temp.getDue());
                tableTasks.getSelectionModel().getSelectedItem().setDescription(
                        temp.getDescription());

                /* Update contexts */
                tableTasks.getSelectionModel().getSelectedItem().clearContext();
                for (String item : temp.getContext()) {
                    tableTasks.getSelectionModel().getSelectedItem().addToContext(item);
                    tasks.addContext(item);
                }

                /* Update projects */
                tableTasks.getSelectionModel().getSelectedItem().clearProject();
                for (String item : temp.getProject()) {
                    tableTasks.getSelectionModel().getSelectedItem().addToProject(item);
                    tasks.addProject(item);
                }
            }
        }
    }



    /**
     * Handle a click on the "done" button: If a task is selected mark this task as done.
     */
    @FXML
    private void handleBtnDoneClick() {
        if (tableTasks.getSelectionModel().getSelectedIndex() != -1) {
            tableTasks.getSelectionModel().getSelectedItem().setDone(true);
        }
    }



    /**
     * Handle a click on the "delete" button: If a task is selected delete this task from the list.
     */
    @FXML
    private void handleBtnDeleteClick() {
        if (tableTasks.getSelectionModel().getSelectedIndex() != -1) {

            /* Confirmation dialog */
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle(translations.getString("dialog.delete.title"));
            alert.setHeaderText(translations.getString("dialog.delete.header"));
            alert.setContentText(translations.getString("dialog.delete.content"));
            Optional<ButtonType> choice = alert.showAndWait();

            /* Confirm deleting of the task */
            if (choice.get() == ButtonType.OK) {
                long deleteTaskId = tableTasks.getSelectionModel().getSelectedItem().getTaskId();
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
            }
        }
    }



    /**
     * Handle a click on the "settings" button: TBD.
     */
    @FXML
    private void handleBtnSettingsClick() {
        System.out.println("SETTINGS");
    }



    /**
     * Handle a click on the "info" button: show the info / about dialog.
     */
    @FXML
    private void handleBtnInfoClick() {
        aboutDialog.showAndWait();
    }



    /**
     * Load a task list from file. If the file name is null or an empty string no task list
     * will be loaded. An error message is shown when the task list could not be opened.
     *
     * @param file File name of the task list
     */
    public void loadTaskList(final String file) {
        if (file != null && !file.isEmpty() && !tasks.loadTaskList(file)) {
            Alert message = new Alert(AlertType.ERROR);
            newEditController.initDialog(message, "dialog.read.title", "dialog.read.header",
                    "dialog.read.content");
            message.showAndWait();
        }
    }



    /**
     * Filter an item (= task) in the table and check if should be visible or hidden.
     *
     * @param item The item (= task) to check
     * @return Result of the check; true = show item in table, false = hide item
     */
    private boolean filterTableItem(Task item) {
        boolean done = true;
        /* Only show done tasks when check box is checked */
        if (!chkbxDone.isSelected() && item.isDone()) {
            done = false;
        }

        String selectedContext = cbxContext.getSelectionModel().getSelectedItem();
        boolean context = false;

        /* Show task when "All Contexts" is selected or when "Without Context" is selected and
           task has no context or when task has context that matches selected context */
        if (selectedContext.equals(translations.getString("filter.context.all"))) {
            context = true;
        } else if (selectedContext.equals(translations.getString("filter.context.without"))
                && item.getContext().isEmpty()) {
            context = true;
        } else {
            for (String itemContext : item.getContext()) {
                if (itemContext.equals(selectedContext)) {
                    context = true;
                    break;
                }
            }
        }

        String selectedProject = cbxProject.getSelectionModel().getSelectedItem();
        boolean project = false;

        /* Show task when "All Projects" is selected or when "Without Project" is selected
           and task has no project or when task has project that matches selected project */
        if (selectedProject.equals(translations.getString("filter.project.all"))) {
            project = true;
        } else if (selectedProject.equals(translations.getString("filter.project.without"))
                && item.getProject().isEmpty()) {
            project = true;
        } else {
            for (String itemProject : item.getProject()) {
                if (itemProject.equals(selectedProject)) {
                    project = true;
                    break;
                }
            }
        }

        /* Combine the three filters and get final filter result */
        return done && context && project;
    }

}
