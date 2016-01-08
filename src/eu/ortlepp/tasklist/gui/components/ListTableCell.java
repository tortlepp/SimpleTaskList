package eu.ortlepp.tasklist.gui.components;

import java.util.List;

import eu.ortlepp.tasklist.model.Task;
import javafx.scene.control.TableCell;

/**
 * A custom renderer for list columns in the table. The whole list is shown in a single table cell, each list item in one line.
 *
 * @author Thorsten Ortlepp
 */
public class ListTableCell extends TableCell<Task, List<String>> {

    /**
     * Customize the appearance of the data shown in the table. The list is converted into a string with list items and line breaks. Each list item becomes one line in the string.
     *
     * @param item The item for the cell; in this case a list of strings
     * @param empty Indicator if the cell contains data or is empty
     */
    @Override
    protected void updateItem(final List<String> item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty || item.isEmpty()) {
            setText(null);
        } else {
            StringBuilder text = new StringBuilder();

            /* One item per line */
            for (int i = 0; i < item.size(); i++) {
                if (i > 0) {
                    text.append(System.lineSeparator());
                }
                text.append(item.get(i));
            }

            setText(text.toString());
        }
    }

}
