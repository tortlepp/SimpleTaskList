package eu.ortlepp.tasklist.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Bean for a single task. It contains all data of the task.
 *
 * @author Thorsten Ortlepp
 */
public class Task {

    /** Static ID counter for task IDs; always contains the value of the last used task ID. */
    private static int id;


    /** Internal ID to identify the task. */
    private final int taskId;


    /** The priority of the task. An uppercase letter A - Z. */
    private StringProperty priority;


    /** The status of the task. Either it is already done (true) or not yet done (false).
        A ChangeListener is connected to adjust priority and completion date when the status changes. */
    private BooleanProperty done;


    /** The creation date of the task. */
    private ObjectProperty<LocalDate> creation;


    /** The completion date of the task. */
    private ObjectProperty<LocalDate> completion;


    /** The due date of the task. */
    private ObjectProperty<LocalDate> due;


    /** A list of all projects of the task. */
    private ObjectProperty<List<String>> project;


    /** A list of all contexts of the task. */
    private ObjectProperty<List<String>> context;


    /** The description / text of the task. */
    private StringProperty description;


    /** Additional meta data of the task. In the todo.txt file meta data is stored as key:value. */
    private MapProperty<String, String> metadata;


    /** Pattern for reading dates from a string. During initialization the pattern is set to yyyy-MM-dd. */
    private final DateTimeFormatter formatter;



    /**
     * Initialize the formatter pattern. All other properties are initialized with empty or default values.
     */
    public Task() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /* Set task ID */
        id++;
        taskId = id;

        /* Initialize with default values */
        priority = new SimpleStringProperty("");
        done = new SimpleBooleanProperty(false);
        creation = new SimpleObjectProperty<LocalDate>(LocalDate.MIN);
        completion = new SimpleObjectProperty<LocalDate>(LocalDate.MIN);
        due = new SimpleObjectProperty<LocalDate>(LocalDate.MIN);
        project = new SimpleObjectProperty<List<String>>(new ArrayList<String>());
        context = new SimpleObjectProperty<List<String>>(new ArrayList<String>());
        description = new SimpleStringProperty("");
        metadata = new SimpleMapProperty<String, String>();

        /* Listener for changes */
        done.addListener(new DoneListener());
    }



    /**
     * Reset the ID counter to 0. Use only when a task list is (re)initialized!
     */
    public static void resetId() {
        id = 0;
    }



    /**
     * Returns the internal ID of the task.
     *
     * @return The internal ID of the task
     */
    public int getTaskId() {
        return taskId;
    }



    /**
     * Getter for the priority of the task.
     *
     * @return The priority of the task (an uppercase letter A - Z)
     */
    public String getPriority() {
        return priority.get();
    }



    /**
     * Setter for the priority of the task.
     *
     * @param priority The priority of the task (an uppercase letter A - Z)
     */
    public void setPriority(final String priority) {
        this.priority.set(priority);
    }



    /**
     * Property for the priority of the task.
     *
     * @return The priority of the task as Property
     */
    public StringProperty priorityProperty() {
        return priority;
    }



    /**
     * Getter for the status of the task.
     *
     * @return The status of the task; either it is already done (true) or not yet done (false)
     */
    public Boolean isDone() {
        return done.get();
    }



    /**
     * Setter for the status of the task.
     *
     * @param done The status of the task; either it is already done (true) or not yet done (false)
     */
    public void setDone(final Boolean done) {
        this.done.set(done);


    }



    /**
     * Property for the status of the task.
     *
     * @return The status of the task as property
     */
    public BooleanProperty doneProperty() {
        return done;
    }



    /**
     * Getter for the creation date of the task.
     *
     * @return The creation date of the task
     */
    public LocalDate getCreation() {
        return creation.get();
    }



    /**
     * Setter for the creation date of the task.
     *
     * @param creation The creation date of the task as string (expected format YYYY-MM-DD)
     */
    public void setCreation(final String creation) {
        this.creation.set(LocalDate.parse(creation, formatter));
    }



    /**
     * Property for the creation date of the task.
     *
     * @return The creation date of the task as property
     */
    public ObjectProperty<LocalDate> creationProperty() {
        return creation;
    }



    /**
     * Getter for the completion date of the task.
     *
     * @return The completion date of the task
     */
    public LocalDate getCompletion() {
        return completion.get();
    }



    /**
     * Setter for the completion date of the task.
     *
     * @param creation The completion date of the task as string (expected format YYYY-MM-DD)
     */
    public void setCompletion(final String completion) {
        this.completion.set(LocalDate.parse(completion, formatter));
    }



    /**
     * Setter for the completion date of the task.
     *
     * @param creation The completion date of the task
     */
    public void setCompletion(final LocalDate completion) {
        this.completion.set(completion);
    }



    /**
     * Property for the completion date of the task.
     *
     * @return The completion date of the task as property
     */
    public ObjectProperty<LocalDate> completionProperty() {
        return completion;
    }



    /**
     * Getter for the due date of the task.
     *
     * @return The due date of the task
     */
    public LocalDate getDue() {
        return due.get();
    }



    /**
     * Setter for the due date of the task.
     *
     * @param creation The due date of the task as string (expected format YYYY-MM-DD)
     */
    public void setDue(final String due) {
        this.due.set(LocalDate.parse(due, formatter));
    }



    /**
     * Property for the due date of the task.
     *
     * @return The due date of the task as property
     */
    public ObjectProperty<LocalDate> dueProperty() {
        return due;
    }



    /**
     * Getter for the list of projects of the task.
     *
     * @return A list of projects of the task
     */
    public List<String> getProject() {
        return project.get();
    }



    /**
     * Setter for the list of projects of the task. Adds one item to the list.
     *
     * @param project One project of the task
     */
    public void addToProject(final String project) {
        this.project.get().add(project);
    }



    /**
     * Property for the list of projects of the task.
     *
     * @return A list of projects of the task as property
     */
    public ObjectProperty<List<String>> projectProperty() {
        return project;
    }



    /**
     * Getter for the list of contexts of the task.
     *
     * @return A list of contexts of the task
     */
    public List<String> getContext() {
        return context.get();
    }



    /**
     * Setter for the list of contexts of the task. Adds one item to the list.
     *
     * @param project One context of the task
     */
    public void addToContext(final String context) {
        this.context.get().add(context);
    }



    /**
     * Property for the list of contexts of the task.
     *
     * @return A list of contexts of the task as property
     */
    public ObjectProperty<List<String>> contextProperty() {
        return context;
    }



    /**
     * Getter for the description / text of the task.
     *
     * @return The description / text of the task
     */
    public String getDescription() {
        return description.get();
    }



    /**
     * Setter for the description / text of the task.
     *
     * @param description The description / text of the task
     */
    public void setDescription(final String description) {
        this.description.set(description);
    }



    /**
     * Property for the description / text of the task.
     *
     * @return The description / text of the task as property
     */
    public StringProperty descriptionProperty() {
        return description;
    }



    /**
     * Getter for the map of additional meta data of the task.
     *
     * @return Additional meta data of the task
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }


    /**
     * Add a key-value-pair to the map of additional meta data of the task.
     * In the todo.txt file meta data is stored as key:value.
     * If the key is due and the value is formatted as YYYY-MM-DD the meta data is not added to the map but the value stored in due as due date.
     *
     * @param key The key of the meta data
     * @param value The value of the meta data
     */
    public void addToMetadata(final String key, final String value) {
        if (key.equalsIgnoreCase("DUE") && value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            this.setDue(value);
        } else {
            this.metadata.put(key, value);
        }
    }


    ///// ----- Inner classes ----- \\\\\


    /**
     * Inner class: A change listener for the boolean done property. Changes the status and the completion date when done changes its value.
     *
     * @author Thorsten Ortlepp
     */
    private class DoneListener implements ChangeListener<Boolean> {

        /** Store the previous priority. */
        private String lastPriority = "";

        /**
         * Triggered when the value of done changes; changes priority and completion date according to the new value of done.
         * If done becomes true the priority is set to "x" and the completion date is set to the current date.
         * If done becomes false the priority is set back to its previous value if available, otherwise it will be just cleared. The completion date is set to the default empty value, the minimum date.
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue) {
                lastPriority = getPriority();
                setPriority("x");
                setCompletion(LocalDate.now());
            } else {

                if (lastPriority.equals("x")) {
                    setPriority("");
                } else {
                    setPriority(lastPriority);
                }

                setCompletion(LocalDate.MIN);
            }
        }

    }



}
