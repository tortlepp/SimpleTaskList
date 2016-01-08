package eu.ortlepp.tasklist.logic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.ortlepp.tasklist.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller for the task lists. It manages all functionality of the list: adding tasks, editing tasks, etc. .
 *
 * @author Thorsten Ortlepp
 */
public class TaskController {


    /** List to store all tasks. Each element of the list is a single task. */
    private final ObservableList<Task> tasklist;


    /**
     * Initialize the task list, an empty list ist created.
     */
    public TaskController() {
        tasklist = FXCollections.observableArrayList();
    }



    /**
     * Getter for the task list. Gives other classes / objects access to the list.
     *
     * @return The task list
     */
    public ObservableList<Task> getTaskList() {
        return tasklist;
    }



    /**
     * Read a todo.txt file and transform its contents into tasks. The tasks list will be cleared before and then the read tasks are added to the list.
     * If the file contains a BOM it will be omitted.
     *
     * @param file The todo.txt file to be read
     * @return Success flag: true if reading the file was successful, false if there was an error while reading the file
     */
    public boolean loadTaskList(final String file) {
        if (new File(file).exists()) {

            try {
                /* Read file line by line */
                List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);

                tasklist.clear();

                for (String line : lines) {

                    /* Remove BOM if it exists */
                    if (line.startsWith("\uFEFF")) {
                        line = line.replaceFirst("\uFEFF", "");
                    }

                    /* Add Task to list */
                    tasklist.add(parseTask(line));
                }

                return true;

              } catch (IOException ex) {
                  System.err.println(ex.getMessage());
              }
        } else {
            System.err.println("The file " + file + " does not exist");
        }

        return false;
    }



    /**
     * Parse a line from a saved file and transform it to a data structure object.
     *
     * @param line A line from a todo.txt file
     * @return The line transformed into a data structure object
     */
    private Task parseTask(final String line) {
        Task task = new Task();

        /* Split line */
        List<String> elements = new ArrayList<String>(Arrays.asList(line.split("\\s")));

        /* priority or done */
        if (!elements.isEmpty()) {
            if (elements.get(0).matches("\\([A-Z]\\)")) {
                task.setPriority(elements.get(0).substring(1, 2));
                elements.remove(0);
            } else if (elements.get(0).matches("x")) {
                task.setPriority("x");
                task.setDone(true);
                elements.remove(0);
            }
        }

        /* completion or creation date */
        if (!elements.isEmpty()) {
            if (elements.get(0).matches("\\d{4}-\\d{2}-\\d{2}")) {
                if (task.isDone()) {
                    task.setCompletion(elements.get(0));
                } else {
                    task.setCreation(elements.get(0));
                }
                elements.remove(0);
            }
        }

        /* creation date for tasks mared as done */
        if (!elements.isEmpty() && task.isDone() && elements.get(0).matches("\\d{4}-\\d{2}-\\d{2}")) {
            task.setCreation(elements.get(0));
            elements.remove(0);
        }

        StringBuilder description = new StringBuilder();

        /* Search for projects, contexts and metadata; read description  */
        for (String element : elements) {
            if (element.matches("\\+\\S+")) {
                task.addToProject(element.substring(1));
            } else if (element.matches("@\\S+")) {
                task.addToContext(element.substring(1));
            } else if (element.matches("\\S+:\\S+")) {
                String[] meta = element.split(":");
                task.addToMetadata(meta[0], meta[1]);
            } else {
                description.append(element).append(" ");
            }
        }

        task.setDescription(description.toString().trim());

        return task;
    }

}
