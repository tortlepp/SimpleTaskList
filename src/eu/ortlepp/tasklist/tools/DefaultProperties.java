package eu.ortlepp.tasklist.tools;

/**
 * Tool class with default values / constants for all properties.
 *
 * @author Thorsten Ortlepp
 */
public final class DefaultProperties {

    /** Default value for automatic saving of task lists. */
    public static final boolean AUTOMATIC_SAVE = true;


    /** Default interval between automatic savings. Value is given in minutes. */
    public static final int AUTOMATIC_SAVE_INTERVAL = 10;


    /** Default value for automatic saving of task list when the application is closed. */
    public static final boolean SAVE_ON_CLOSE = true;


    /** The file name of the standard task list (which is opened when the application starts). */
    public static final String STANDARD_TASKLIST = "";


    /** The file name of the archive file for completed tasks. */
    public static final String ARCHIVE_FILE = "done.txt";


    /** Default value for showing tooltips / hints in the main window. */
    public static final boolean SHOW_TOOLTIPS = true;


    /** Standard letter for the shortcut to open a file. */
    public static final String SHORTCUT_KEY_OPEN = "O";


    /** Standard letter for the shortcut to save a file. */
    public static final String SHORTCUT_KEY_SAVE = "S";


    /** Standard letter for the shortcut to create a new task. */
    public static final String SHORTCUT_KEY_NEW = "N";


    /** Standard letter for the shortcut to edit the selected task. */
    public static final String SHORTCUT_KEY_EDIT = "E";


    /** Standard letter for the shortcut to mark the selected task as done. */
    public static final String SHORTCUT_KEY_DONE = "X";


    /** Standard letter for the shortcut to delete the selected task. */
    public static final String SHORTCUT_KEY_DELETE = "D";


    /** Standard letter for the shortcut to move completed task to the archive. */
    public static final String SHORTCUT_KEY_MOVE = "A";


    /** Default value for the maximization of the main window. */
    public static final boolean WINDOW_MAXIMIZED = false;


    /** Default width of the main window. Value is given in pixels. */
    public static final int WINDOW_WIDTH = 800;


    /** Default height of the main window. Value is given in pixels. */
    public static final int WINDOW_HEIGHT = 600;


    /** Default x position of the main window. The value -1 indicates that no position is set. */
    public static final int WINDOW_POS_X = -1;


    /** Default y position of the main window. The value -1 indicates that no position is set. */
    public static final int WINDOW_POS_Y = -1;


    /**
     * Private constructor for tool class (without functionality).
     */
    private DefaultProperties() {
        /* Nothing happens here */
    }

}
