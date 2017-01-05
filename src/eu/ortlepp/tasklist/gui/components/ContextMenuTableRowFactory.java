package eu.ortlepp.tasklist.gui.components;

import eu.ortlepp.tasklist.SimpleTaskList;
import eu.ortlepp.tasklist.gui.MainWindowController;
import eu.ortlepp.tasklist.model.Task;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Custom row factory for rows in a TableView. Adds a context menu to the rows
 * of the TableView that contain a task (and are not empty). The context menu
 * contains items to edit and delete the task, to set a task as done and to copy
 * the description of the task to the clipboard.
 *
 * @author Thorsten Ortlepp
 */
public class ContextMenuTableRowFactory implements Callback<TableView<Task>, TableRow<Task>> {

    /** Translated captions for menu items. */
    private final ResourceBundle translations;

    /** Reference to the controller of the main window. */
    private final MainWindowController controller;


    /**
     * Initialize the row factory. Set the controller and load the translations.
     *
     * @param controller Reference to the controller of the main window
     */
    public ContextMenuTableRowFactory(final MainWindowController controller) {
        this.controller = controller;

        try {
            translations = ResourceBundle.getBundle(SimpleTaskList.TRANSLATION);
        } catch (MissingResourceException ex) {
            throw new RuntimeException("Translation is not available", ex);
        }
    }


    /**
     * Constructs the context menu and adds it to the row if the row is not empty.
     *
     * @param table The TableView which contains the row(s)
     * @return The created row
     */
    @Override
    public TableRow<Task> call(final TableView<Task> table) {
        final TableRow<Task> row = new TableRow<Task>();
        final ContextMenu contextMenu = new ContextMenu();

        /* Menu item to open an editor window for the task */
        final MenuItem menuItemEdit = new MenuItem(translations.getString("context.edit"));
        menuItemEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                controller.editTask(row.getIndex());
            }
        });

        /* Menu item to mark the task as done */
        final MenuItem menuItemDone = new MenuItem(translations.getString("context.done"));
        menuItemDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                controller.setTaskDone(row.getIndex());
            }
        });

        /* Menu item to copy the description of the task to the clipboard */
        final MenuItem menuItemCopy = new MenuItem(translations.getString("context.copy"));
        menuItemCopy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                final StringSelection copyText = new StringSelection(row.getItem().getDescription());
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(copyText, copyText);
            }
        });

        /* Menu item to delete the task */
        final MenuItem menuItemDelete = new MenuItem(translations.getString("context.delete"));
        menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                controller.deleteTask(row.getIndex());
            }
        });

        /* Add menu items to the context menu */
        contextMenu.getItems().addAll(menuItemEdit, menuItemDone, menuItemCopy, menuItemDelete);

        /* Display context menu only for tasks (and not for empty rows) */
        row.contextMenuProperty().bind(Bindings
                .when(Bindings.isNotNull(row.itemProperty()))
                .then(contextMenu)
                .otherwise((ContextMenu) null));

        return row;
    }

}
