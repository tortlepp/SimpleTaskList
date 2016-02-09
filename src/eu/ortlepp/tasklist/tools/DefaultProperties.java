package eu.ortlepp.tasklist.tools;

/**
 * Tool class with default values / constants for all properties.
 *
 * @author Thorsten Ortlepp
 */
public final class DefaultProperties {

    /** Default value for automatic saving of task lists. */
    protected static final boolean AUTOMATIC_SAVE = true;


    /** Default interval between automatic savings. Value is given in in minutes. */
    protected static final int AUTOMATIC_SAVE_INTERVAL = 10;


    /** Default value for automatic saving of task list when the application is closed. */
    protected static final boolean SAVE_ON_CLOSE = true;


    /** The file name of the standard task list (which is opened when the application starts). */
    protected static final String STANDARD_TASKLIST = "";


    /** The file name of the archive file for completed tasks. */
    protected static final String ARCHIVE_FILE = "done.txt";


    /** Default value for showing tooltips / hints in the main window. */
    protected static final boolean SHOW_TOOLTIPS = true;


    /**
     * Private constructor for tool class (without functionality).
     */
    private DefaultProperties() {
        /* Nothing happens here */
    }

}
