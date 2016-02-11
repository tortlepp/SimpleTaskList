package eu.ortlepp.tasklist.gui.components;

import eu.ortlepp.tasklist.model.Task;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

/**
 * A custom renderer for the priority column in the table. Some priorities are shown colored:
 * Priority A in red, priority B in yellow, priority C in green and all other priorities in black.
 * Also the content of the column is centered.
 *
 * @author Thorsten Ortlepp
 */
public class PriorityTableCell extends TableCell<Task, String> {

    /**
     * Customize the appearance of the data shown in the table. The content is centered
     * and the font color is set.
     *
     * @param item The item for the cell; in this case a string
     *     (a single uppercase letter or empty)
     * @param empty Indicator if the cell contains data or is empty
     */
    @Override
    protected void updateItem(final String item, final boolean empty) {
        super.updateItem(item, empty);

        /* Center content */
        setStyle( "-fx-alignment: CENTER;");

        if (item == null || empty) {
            setText(null);

        } else if (item.equals("x")) {
            /* Task marked as done */
            setTextFill(Color.BLACK);
            setText("\u00D7");

        } else {
            /* Tasks with or without priority */
            setText(item);
            switch (item) {
                case "A": setTextFill(Color.RED);
                          break;
                case "B": setTextFill(Color.YELLOW);
                          break;
                case "C": setTextFill(Color.GREEN);
                          break;
                default: setTextFill(Color.BLACK);
                         break;
            }

        }
    }

}
