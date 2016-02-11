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


    /** Standard letter for the shortcut to open a file. */
    protected static final String SHORTCUT_KEY_OPEN = "O";


    /** Standard letter for the shortcut to save a file. */
    protected static final String SHORTCUT_KEY_SAVE = "S";


    /** Standard letter for the shortcut to create a new task. */
    protected static final String SHORTCUT_KEY_NEW = "N";


    /** Standard letter for the shortcut to edit the selected task. */
    protected static final String SHORTCUT_KEY_EDIT = "E";


    /** Standard letter for the shortcut to mark the selected task as done. */
    protected static final String SHORTCUT_KEY_DONE = "X";


    /** Standard letter for the shortcut to delete the selected task. */
    protected static final String SHORTCUT_KEY_DELETE = "D";


    /** Standard letter for the shortcut to move completed task to the archive. */
    protected static final String SHORTCUT_KEY_MOVE = "A";


    /**
     * Private constructor for tool class (without functionality).
     */
    private DefaultProperties() {
        /* Nothing happens here */
    }

}
