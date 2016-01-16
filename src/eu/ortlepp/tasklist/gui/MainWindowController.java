package eu.ortlepp.tasklist.gui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import eu.ortlepp.tasklist.SimpleTaskList;
import eu.ortlepp.tasklist.gui.components.DateTableCell;
import eu.ortlepp.tasklist.gui.components.DescriptionTableCell;
import eu.ortlepp.tasklist.gui.components.ListTableCell;
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

/**
 * Controller for the main window. Handles all actions of the main window.
 *
 * @author Thorsten Ortlepp
 */
public class MainWindowController {


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
    private TableColumn<Task, List<String>> columnContext;


    /** Column for the table to show the project of the tasks. */
    @FXML
    private TableColumn<Task, List<String>> columnProject;


    /** Translated captions and tooltips for the GUI. */
    private final ResourceBundle translations;


    /** Controller for the loaded task list file. */
    private final TaskController tasks;


    /** The primary stage of the main window. */
    private Stage stage;


    /** The dialog window to add new tasks or edit existing tasks. */
    private Stage newEditDialog;


    /** The controller of the new / edit dialog. */
    private NewEditDialogController newEditController;


    /**
     * Initialize controller by loading the translated captions and tooltips for the GUI components.
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
        columnContext.setCellFactory(column -> new ListTableCell());
        columnProject.setCellFactory(column -> new ListTableCell());

        /* Show data from the model in the table */
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().doneProperty());
        columnPriority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        columnDue.setCellValueFactory(cellData -> cellData.getValue().dueProperty());
        columnDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        columnContext.setCellValueFactory(cellData -> cellData.getValue().contextProperty());
        columnProject.setCellValueFactory(cellData -> cellData.getValue().projectProperty());

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
        /* Load the translation for the GUI */
        ResourceBundle bundle = PropertyResourceBundle.getBundle(SimpleTaskList.TRANSLATION);

        /* Initialize new / edit dialog */
        newEditDialog = new Stage();

        /* Load FXML and initialize get access to the controller */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("eu/ortlepp/tasklist/fxml/neweditdialog.fxml"), bundle);
            Parent root = loader.load();
            newEditDialog.setScene(new Scene(root));
            newEditController = loader.getController();
            newEditController.setStage(newEditDialog);
        } catch (IOException ex) {
            System.err.println("Initialization of the new / edit dialog failed: " + ex.getMessage());
        }

        /* Set properties of the dialog */
        newEditDialog.initModality(Modality.APPLICATION_MODAL);
        newEditDialog.initOwner(stage);
        newEditDialog.initStyle(StageStyle.UTILITY);
        newEditDialog.setResizable(false);
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
            tasks.loadTaskList(file.getAbsolutePath());
        }
    }



    /**
     * Handle a click on the "save" button: TBD.
     */
    @FXML
    private void handleBtnSaveClick() {
        System.out.println("SAVE");
    }



    /**
     * Handle a click on the "new" button: Open the new task dialog and then add one or more tasks to the list.
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
     * Handle a click on the "edit" button: Open edit dialog and after editing update the edited task.
     */
    @FXML
    private void handleBtnEditClick() {
        if (tableTasks.getSelectionModel().getSelectedIndex() != -1) {
            /* Open edit dialog */
            newEditController.setEditDialog(tableTasks.getSelectionModel().getSelectedItem(), tasks.getContextList(), tasks.getProjectList());
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
                tableTasks.getSelectionModel().getSelectedItem().setDescription(temp.getDescription());

                /* Update contexts */
                tableTasks.getSelectionModel().getSelectedItem().getContext().clear();
                for (String item : temp.getContext()) {
                    tableTasks.getSelectionModel().getSelectedItem().addToContext(item);
                    tasks.addContext(item);
                }

                /* Update projects */
                tableTasks.getSelectionModel().getSelectedItem().getProject().clear();
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
            if (choice.get() == ButtonType.OK){
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
     * Handle a click on the "info" button: TBD.
     */
    @FXML
    private void handleBtnInfoClick() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(translations.getString("dialog.about.title"));
        alert.setHeaderText(translations.getString("dialog.about.header"));
        alert.setContentText(translations.getString("dialog.about.content"));
        alert.showAndWait();
    }



    /**
     * Load a task list from file. If the file name is null or an empty string no task list will be loaded.
     * An error message is shown when the task list could not be opened.
     *
     * @param file File name of the task list
     */
    public void loadTaskList(final String file) {
        if (file != null && !file.isEmpty() && !tasks.loadTaskList(file)) {
            //TODO
            System.err.println("LOADING FILE FAILED");
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

        /* Show task when "All Contexts" is selected or when "Without Context" is selected and task has no context or when task has context that matches selected context */
        if (selectedContext.equals(translations.getString("filter.context.all"))) {
            context = true;
        } else if (selectedContext.equals(translations.getString("filter.context.without")) && item.getContext().isEmpty()) {
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

        /* Show task when "All Projects" is selected or when "Without Project" is selected and task has no project or when task has project that matches selected project */
        if (selectedProject.equals(translations.getString("filter.project.all"))) {
            project = true;
        } else if (selectedProject.equals(translations.getString("filter.project.without")) && item.getProject().isEmpty()) {
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
