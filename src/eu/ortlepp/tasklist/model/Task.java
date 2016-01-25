package eu.ortlepp.tasklist.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Bean for a single task. It contains all data of the task.
 *
 * @author Thorsten Ortlepp
 */
public class Task {

    /** ID counter for task IDs; always contains the value of the last used task ID. */
    private static AtomicLong id = new AtomicLong(0);


    /** Internal ID to identify the task. */
    private final long taskId;


    /** The priority of the task. An uppercase letter A - Z. */
    private final StringProperty priority;


    /**
     * The status of the task. Either it is already done (true) or not yet done (false).
     * A ChangeListener is connected to adjust priority and completion date when the
     * status changes.
     */
    private final BooleanProperty done;


    /** The creation date of the task. */
    private final ObjectProperty<LocalDate> creation;


    /** The completion date of the task. */
    private final ObjectProperty<LocalDate> completion;


    /** The due date of the task. */
    private final ObjectProperty<LocalDate> due;


    /** A list of all projects of the task. */
    private final ObjectProperty<List<String>> project;


    /** The List of all projects as string. */
    private final StringProperty projectString;


    /** A list of all contexts of the task. */
    private final ObjectProperty<List<String>> context;


    /** The list of all contexts as string. */
    private final StringProperty contextString;


    /** The description / text of the task. */
    private final StringProperty description;


    /** Additional meta data of the task. In the todo.txt file meta data is stored as key:value. */
    private final Map<String, String> metadata;


    /**
     * Pattern for reading dates from a string.
     * During initialization the pattern is set to yyyy-MM-dd.
     */
    private final DateTimeFormatter formatter;



    /**
     * Initialize the formatter pattern. All other properties are initialized with empty
     * or default values.
     */
    public Task() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /* Set task ID */
        taskId = id.incrementAndGet();

        /* Initialize with default values */
        priority = new SimpleStringProperty("");
        done = new SimpleBooleanProperty(false);
        creation = new SimpleObjectProperty<LocalDate>(LocalDate.MIN);
        completion = new SimpleObjectProperty<LocalDate>(LocalDate.MIN);
        due = new SimpleObjectProperty<LocalDate>(LocalDate.MIN);
        project = new SimpleObjectProperty<List<String>>(new ArrayList<String>());
        projectString = new SimpleStringProperty(listToString(getProject()));
        context = new SimpleObjectProperty<List<String>>(new ArrayList<String>());
        contextString = new SimpleStringProperty(listToString(getContext()));
        description = new SimpleStringProperty("");
        metadata = new HashMap<String, String>();

        /* Listener for changes */
        done.addListener(new DoneListener());
    }



    /**
     * Copy constructor to "clone" a task.
     *
     * @param task The source task
     */
    public Task(final Task task) {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /* Set task ID */
        taskId = id.incrementAndGet();

        /* Initialize with copied values */
        priority = new SimpleStringProperty(task.getPriority());
        done = new SimpleBooleanProperty(task.isDone());
        creation = new SimpleObjectProperty<LocalDate>(task.getCreation());
        completion = new SimpleObjectProperty<LocalDate>(task.getCompletion());
        due = new SimpleObjectProperty<LocalDate>(task.getDue());
        project = new SimpleObjectProperty<List<String>>(new ArrayList<String>());
        project.get().addAll(task.getProject());
        projectString = new SimpleStringProperty(listToString(getProject()));
        context = new SimpleObjectProperty<List<String>>(new ArrayList<String>());
        context.get().addAll(task.getContext());
        contextString = new SimpleStringProperty(listToString(getContext()));
        description = new SimpleStringProperty(task.getDescription());
        metadata = new HashMap<String, String>();
        for (final String key : task.getMetadata().keySet()) {
            metadata.put(key, task.getMetadata().get(key));
        }

        /* Listener for changes */
        done.addListener(new DoneListener());
    }



    /**
     * Reset the ID counter to 0. Use only when a task list is (re)initialized!
     */
    public static void resetId() {
        id.set(0);
    }



    /**
     * Returns the internal ID of the task.
     *
     * @return The internal ID of the task
     */
    public long getTaskId() {
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
     * @param creation The creation date of the task
     */
    public void setCreation(final LocalDate creation) {
        this.creation.set(creation);
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
     * @param completion The completion date of the task as string (expected format YYYY-MM-DD)
     */
    public void setCompletion(final String completion) {
        this.completion.set(LocalDate.parse(completion, formatter));
    }



    /**
     * Setter for the completion date of the task.
     *
     * @param completion The completion date of the task
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
     * @param due The due date of the task
     */
    public void setDue(final LocalDate due) {
        this.due.set(due);
    }



    /**
     * Setter for the due date of the task.
     *
     * @param due The due date of the task as string (expected format YYYY-MM-DD)
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
    public final List<String> getProject() {
        return project.get();
    }



    /**
     * Remove all items from the list of projects of the task.
     */
    public void clearProject() {
        project.get().clear();
        projectString.set("");
    }



    /**
     * Setter for the list of projects of the task. Adds one item to the list.
     *
     * @param project One project of the task
     */
    public void addToProject(final String project) {
        this.project.get().add(project);
        this.projectString.set(listToString(getProject()));
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
     * Property for a string of all projects of the task.
     *
     * @return A string of all projects of the task as property
     */
    public StringProperty projectStringProperty() {
        return projectString;
    }



    /**
     * Getter for the list of contexts of the task.
     *
     * @return A list of contexts of the task
     */
    public final List<String> getContext() {
        return context.get();
    }



    /**
     * Remove all items from the list of contexts of the task.
     */
    public void clearContext() {
        context.get().clear();
        contextString.set("");
    }



    /**
     * Setter for the list of contexts of the task. Adds one item to the list.
     *
     * @param context One context of the task
     */
    public void addToContext(final String context) {
        this.context.get().add(context);
        this.contextString.set(listToString(getContext()));
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
     * Property for a string of all contexts of the task.
     *
     * @return A string of all contexts of the task as property
     */
    public StringProperty contextStringProperty() {
        return contextString;
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
     * If the key is due and the value is formatted as YYYY-MM-DD the meta data
     * is not added to the map but the value stored in due as due date.
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



    /**
     * Convert a list of string into a single string. After each list item a line break
     * is inserted into the string.
     *
     * @param list The list to be converted into a string
     * @return The created string; each list item is separated by a line break
     */
    private String listToString(final List<String> list) {
        final StringBuilder text = new StringBuilder();

        /* One item per "line" */
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                text.append(System.lineSeparator());
            }
            text.append(list.get(i));
        }

        return text.toString();
    }


    ///// ----- Inner classes ----- \\\\\


    /**
     * Inner class: A change listener for the boolean done property. Changes the status
     * and the completion date when done changes its value.
     *
     * @author Thorsten Ortlepp
     */
    private class DoneListener implements ChangeListener<Boolean> {

        /** Store the previous priority. */
        private String lastPriority = "";

        /**
         * Triggered when the value of done changes; changes priority and completion date
         * according to the new value of done. If done becomes true the priority is set to
         * "x" and the completion date is set to the current date. If done becomes false
         * the priority is set back to its previous value if available, otherwise it will be
         * just cleared. The completion date is set to the default empty value, the minimum date.
         */
        @Override
        public void changed(final ObservableValue<? extends Boolean> observable,
               final Boolean oldValue, final  Boolean newValue) {
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
