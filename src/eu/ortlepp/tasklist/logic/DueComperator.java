package eu.ortlepp.tasklist.logic;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * A custom comperator to sort the due column in the correct order.
 * The correct order is youngest date ... oldest date, no due date.
 *
 * @author Thorsten Ortlepp
 */
public class DueComperator implements Comparator<LocalDate> {

    /**
     * Compares two dates.
     *
     * @param due1 The first date
     * @param due2 The second date
     * @return Result of the comparison
     */
    @Override
    public int compare(LocalDate due1, LocalDate due2) {
        return fixDate(due1).compareTo(fixDate(due2));
    }



    /**
     * Fix a date; the minimum date is inverted into the maximum date,
     * all other dates remain the same.
     *
     * @param date The date to be fixed
     * @return The fixed date
     */
    private LocalDate fixDate(LocalDate date) {
        if (date.isEqual(LocalDate.MIN)) {
            return LocalDate.MAX;
        }
        return date;
    }

}
