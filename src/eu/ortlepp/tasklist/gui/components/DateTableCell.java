package eu.ortlepp.tasklist.gui.components;

import eu.ortlepp.tasklist.model.Task;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * A custom renderer for date columns in the table. The date will be formatted
 * according to the set format. If the Date is equal to or before the current
 * date it is displayed colored.
 *
 * @author Thorsten Ortlepp
 */
public class DateTableCell extends TableCell<Task, LocalDate> {


    /** Format for the date. */
    private final DateTimeFormatter formatter;


    /**
     * Initialize the date format for the output of the date in the table.
     */
    public DateTableCell() {
        super();
        formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    }



    /**
     * Customize the appearance of the data shown in the table. The date is formatted
     * according to the format initialized in the constructor. If the Date is equal to
     * or before the current date it is displayed colored.
     *
     * @param item The item for the cell; in this case a date
     * @param empty Indicator if the cell contains data or is empty
     */
    @Override
    protected void updateItem(final LocalDate item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty || item.equals(LocalDate.MIN)) {
            setText(null);
        } else {

            /* Set color depending on the date */
            if (item.isEqual(LocalDate.now())) {
                setTextFill(Color.BLUEVIOLET);
            } else if (item.isBefore(LocalDate.now())) {
                setTextFill(Color.DEEPPINK);
            } else {
                setTextFill(Color.BLACK);
            }

            setText(item.format(formatter));
        }
    }

}
