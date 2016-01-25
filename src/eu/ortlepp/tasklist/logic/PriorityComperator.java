package eu.ortlepp.tasklist.logic;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A custom comperator to sort the priority column in the correct order.
 * The correct order is priority A ... priority Z, no priority, done.
 *
 * @author Thorsten Ortlepp
 */
public class PriorityComperator implements Comparator<String>, Serializable {

    /** Randomly generated ID for Serializable. */
    private static final long serialVersionUID = 8070298259589601364L;


    /**
     * Compares two priorities.
     *
     * @param priority1 The first priority
     * @param priority2 The second priority
     * @return Result of the comparison
     */
    @Override
    public int compare(final String priority1, final String priority2) {
        return getValue(priority1) - getValue(priority2);
    }



    /**
     * Get the value of a priority. A - Z return their numeric ASCII value,
     * empty stings return ASCII value of "Z" + 1 and x (task done) returns
     * ASCII value of "Z" + 2.
     *
     * @param string The string / priority
     * @return The value of the priority
     */
    private int getValue(final String string) {
        if (string.isEmpty()) {
            return 'Z' + 1;
        } else if (string.equals("x")) {
            return 'Z' + 2;
        }
        return (int) string.charAt(0);
    }

}
