package eu.ortlepp.tasklist.gui.components;

import eu.ortlepp.tasklist.model.Task;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

/**
 * A custom renderer for the description column in the table. The content of the table cell
 * is displayed in multiple lines if necessary.
 *
 * @author Thorsten Ortlepp
 */
public class DescriptionTableCell extends TableCell<Task, String> {

    /**
     * Customize the way how text is displayed in the table cell. If necessary the text
     * will be displayed in multiple lines instead of a single line.
     *
     * @param item The item for the cell; in this case a string
     * @param empty Indicator if the cell contains data or is empty
     */
    @Override
    protected void updateItem(final String item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            final Text text = new Text(item);
            text.wrappingWidthProperty().bind(getTableColumn().widthProperty());
            setGraphic(text);
        }
    }

}
