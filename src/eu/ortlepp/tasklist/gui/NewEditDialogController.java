package eu.ortlepp.tasklist.gui;

import eu.ortlepp.tasklist.SimpleTaskList;
import eu.ortlepp.tasklist.model.Task;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the new task and edit task dialog window.
 * Handles all actions of the dialog window.
 *
 * @author Thorsten Ortlepp
 */
public class NewEditDialogController extends AbstractDialogController {


    /** Layout container for buttons. */
    @FXML
    private GridPane gridpaneButtons;


    /** Button to continue / add another task. */
    @FXML
    private Button buttonContinue;


    /** Button to add tasks to the list. */
    @FXML
    private Button buttonDone;


    /** Button to save changes on the edited task. */
    @FXML
    private Button buttonSave;


    /** Combo box to select the priority. */
    @FXML
    private ComboBox<String> comboboxPriority;


    /** Picker to select the creation date. */
    @FXML
    private DatePicker datepickerCreation;


    /** Picker to select the due date. */
    @FXML
    private DatePicker datepickerDue;


    /** Area to insert / edit the description text. */
    @FXML
    private TextArea textareaDescription;


    /** List of all contexts of the task. */
    @FXML
    private ListView<String> listviewContext;


    /** List of all projects of the task. */
    @FXML
    private ListView<String> listviewProject;


    /** List of all existing contexts. */
    private List<String> contexts;


    /** List of all existing projects. */
    private List<String> projects;


    /**
     * The save state of the data. Values: true = save / done was clicked,
     * false = cancel was clicked.
     */
    private boolean saved;


    /** List of all new tasks. */
    private List<Task> newTasks;



    /**
     * Initialize the dialog and its components; load the translations.
     */
    @FXML
    @Override
    protected void initialize() {
        contexts = new ArrayList<String>();
        projects = new ArrayList<String>();
        newTasks = new ArrayList<Task>();

        comboboxPriority.setItems(FXCollections.observableArrayList(
                translations.getString("choice.priority.no"),
                translations.getString("choice.priority.done"),
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
    }



    /**
     * Handle a click on the "cancel" button: hide / close the dialog window.
     * Ask for confirmation if there are tasks that are not yet added to the task list.
     */
    @FXML
    @Override
    protected void handleDialogHide() {
        boolean cancel = true;

        /* Confirm canceling if there are not yet added tasks */
        if (!newTasks.isEmpty()) {
            final Alert confirmation = new Alert(AlertType.CONFIRMATION);
            AbstractDialogController.prepareDialog(confirmation, "dialog.cancel.title", "dialog.cancel.header",
                    "dialog.cancel.content");
            final Optional<ButtonType> choice = confirmation.showAndWait();
            if (choice.get() == ButtonType.CANCEL) {
                cancel = false;
            }
        }

        if (cancel) {
            stage.hide();
        }
    }


    /**
     * Handle a click on the "save" button: set saved and hide / close the dialog window.
     */
    @FXML
    private void handleSaveClick() {
        saved = true;
        stage.hide();
    }



    /**
     * Handle a click on the "continue" button: save the entered data
     * and clear the input components.
     */
    @FXML
    private void handleContinueClick() {

        /* At least enter a task description */
        if (textareaDescription.getText().isEmpty()) {
            final Alert message = new Alert(AlertType.WARNING);
            AbstractDialogController.prepareDialog(message, "dialog.empty.title", "dialog.empty.header",
                    "dialog.empty.content");
            message.showAndWait();
        } else {
            newTasks.add(createTask());
            resetComponents();
        }
    }



    /**
     * Handle a click on the "done" button: save the entered data, set saved
     * and hide / close the dialog window.
     */
    @FXML
    private void handleDoneClick() {
        if (!textareaDescription.getText().isEmpty()) {
            newTasks.add(createTask());
        }

        saved = true;
        stage.hide();
    }



    /**
     * Handle a click on the "select context" button: open a selection dialog
     * and add the selected context to the list.
     */
    @FXML
    private void handleSelectContext() {
        if (!contexts.isEmpty()) {
            final ChoiceDialog<String> choice = new ChoiceDialog<String>(contexts.get(0), contexts);
            AbstractDialogController.prepareDialog(choice, "dialog.context.select.title", "dialog.context.select.header",
                    "dialog.context.select.content");
            final Optional<String> text = choice.showAndWait();
            if (text.isPresent()) {
                listviewContext.getItems().add(text.get());
            }
        }
    }



    /**
     * Handle a click on the "add context" button: open an input dialog
     * and add the inserted context to the list.
     */
    @FXML
    private void handleAddContext() {
        final TextInputDialog input = new TextInputDialog();
        AbstractDialogController.prepareDialog(input, "dialog.context.new.title", "dialog.context.new.header",
                "dialog.context.new.content");
        final Optional<String> text = input.showAndWait();
        if (text.isPresent()) {
            listviewContext.getItems().add(text.get());
        }
    }



    /**
     * Handle a click on the "remove context" button: remove the selected context from the list.
     */
    @FXML
    private void handleRemoveContext() {
        if (listviewContext.getSelectionModel().getSelectedIndex() != -1) {
            listviewContext.getItems().remove(listviewContext.getSelectionModel().getSelectedIndex());
        }
    }



    /**
     * Handle a click on the "select project" button: open a selection dialog
     * and add the selected project to the list.
     */
    @FXML
    private void handleSelectProject() {
        if (!projects.isEmpty()) {
            final ChoiceDialog<String> choice = new ChoiceDialog<String>(projects.get(0), projects);
            AbstractDialogController.prepareDialog(choice, "dialog.project.select.title", "dialog.project.select.header",
                    "dialog.project.select.content");
            final Optional<String> text = choice.showAndWait();
            if (text.isPresent()) {
                listviewProject.getItems().add(text.get());
            }
        }
    }



    /**
     * Handle a click on the "add project" button: open an input dialog and
     * add the inserted project to the list.
     */
    @FXML
    private void handleAddProject() {
        final TextInputDialog input = new TextInputDialog();
        AbstractDialogController.prepareDialog(input, "dialog.project.new.title", "dialog.project.new.header",
                "dialog.project.new.content");
        final Optional<String> text = input.showAndWait();
        if (text.isPresent()) {
            listviewProject.getItems().add(text.get());
        }
    }



    /**
     * Handle a click on the "remove project" button: remove the selected project from the list.
     */
    @FXML
    private void handleRemoveProject() {
        if (listviewProject.getSelectionModel().getSelectedIndex() != -1) {
            listviewProject.getItems().remove(listviewProject.getSelectionModel().getSelectedIndex());
        }
    }



    /**
     * Set dialog to the "new task" mode.
     *
     * @param contexts All existing contexts
     * @param projects All existing projects
     */
    public void setNewDialog(final List<String> contexts, final List<String> projects) {
        gridpaneButtons.getChildren().removeAll(buttonContinue, buttonDone, buttonSave);
        gridpaneButtons.add(buttonDone, 0, 0);
        gridpaneButtons.add(buttonContinue, 1, 0);

        saved = false;
        newTasks.clear();

        initChoiceList(contexts, this.contexts);
        initChoiceList(projects, this.projects);
        resetComponents();
    }



    /**
     * Set dialog to the "edit task" mode. Show the data of the currently selected task
     * in the dialog window.
     *
     * @param task The data of the currently selected task
     * @param contexts All existing contexts
     * @param projects All existing projects
     */
    public void setEditDialog(final Task task, final List<String> contexts, final List<String> projects) {
        gridpaneButtons.getChildren().removeAll(buttonContinue, buttonDone, buttonSave);
        gridpaneButtons.add(buttonSave, 0, 0);

        saved = false;
        newTasks.clear();

        initChoiceList(contexts, this.contexts);
        initChoiceList(projects, this.projects);

        /* Select priority */
        int prioIndex = 0;
        if (task.getPriority() != null && !task.getPriority().isEmpty()) {
            if (task.getPriority().equals("x")) {
                prioIndex = 1;
            } else {
                prioIndex = task.getPriority().charAt(0) - 'A' + 2;
            }
        }

        /* Set task data in dialog window */
        comboboxPriority.getSelectionModel().clearAndSelect(prioIndex);
        initDatePicker(datepickerCreation, task.getCreation());
        initDatePicker(datepickerDue, task.getDue());
        textareaDescription.setText(task.getDescription());
        listviewContext.setItems(FXCollections.observableArrayList(task.getContext()));
        listviewProject.setItems(FXCollections.observableArrayList(task.getProject()));
    }



    /**
     * Initialize a date picker with a date. If the date has the minimum value no date is set.
     *
     * @param picker The date picker component to initialize
     * @param value The date to be set in the picker
     */
    private void initDatePicker(final DatePicker picker, final LocalDate value) {
        if (value.equals(LocalDate.MIN)) {
            picker.setValue(null);
        } else {
            picker.setValue(value);
        }
    }



    /**
     * Initialize a choice list with items. The first two items of the source list
     * (all / no items) are omitted.
     *
     * @param source The source list with all items
     * @param target The list to be initialized
     */
    private void initChoiceList(final List<String> source, final List<String> target) {
        target.clear();
        for (int i = 2; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }



    /**
     * Getter for the save state.
     *
     * @return The save state; true = save, false = canceled
     */
    public boolean isSaved() {
        return saved;
    }



    /**
     * Getter for the edited task data.
     *
     * @return The edited data of the task
     */
    public Task getEditedTask() {
        return createTask();
    }



    /**
     * Getter for all new / added tasks.
     *
     * @return The new / added tasks in a list
     */
    public List<Task> getNewTasks() {
        return newTasks;
    }



    /**
     * Create a task object with data from the dialog window.
     *
     * @return The created task object
     */
    private Task createTask() {
        /* Task object with default values */
        final Task task = new Task();

        /* Set status */
        if (comboboxPriority.getSelectionModel().getSelectedIndex() == 1) {
            task.setPriority("x");
            task.setDone(true);
        } else if (comboboxPriority.getSelectionModel().getSelectedIndex() > 1) {
            task.setPriority(comboboxPriority.getSelectionModel().getSelectedItem());
        }

        /* Set dates if a date was chosen */
        if (datepickerCreation.getValue() != null) {
            task.setCreation(datepickerCreation.getValue());
        }
        if (datepickerDue.getValue() != null) {
            task.setDue(datepickerDue.getValue());
        }

        /* Set lists */
        for (final String item : listviewContext.getItems()) {
            task.addToContext(item);
        }
        for (final String item : listviewProject.getItems()) {
            task.addToProject(item);
        }

        /* Set text */
        task.setDescription(textareaDescription.getText());

        return task;
    }



    /**
     * (Re)Initialize GUI components with empty / default values.
     */
    private void resetComponents() {
        comboboxPriority.getSelectionModel().clearAndSelect(0);
        datepickerCreation.setValue(LocalDate.now());
        datepickerDue.setValue(null);
        textareaDescription.setText("");
        listviewContext.getItems().clear();
        listviewProject.getItems().clear();
    }

}
