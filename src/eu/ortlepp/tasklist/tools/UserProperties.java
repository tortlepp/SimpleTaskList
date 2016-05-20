package eu.ortlepp.tasklist.tools;

import eu.ortlepp.tasklist.SimpleTaskList;

import java.util.prefs.Preferences;

/**
 * Access to the user defined (or default) properties for the application.
 * This class uses the preferences API and is implemented as singleton.
 *
 * @author Thorsten Ortlepp
 */
public final class UserProperties {

    /** An object of this class itself. Needed for singleton implementation. */
    private static UserProperties  properties;


    /** Access to the preferences API. */
    private final Preferences preferences;


    /** Preferences key for the automatic saving property. */
    private static final String KEY_AUTOMATIC_SAVE = "automatic.save";


    /** Preferences key for the automatic saving interval property. */
    private static final String KEY_AUTOMATIC_SAVE_INTERVAL = "automatic.save.interval";


    /** Preferences key for the automatic saving on close property. */
    private static final String KEY_SAVE_ONCLOSE = "save.onclose";


    /** Preferences key for the standard task list property. */
    private static final String KEY_STANDARD_TASKLIST = "standard.tasklist";


    /** Preferences key for the archive file property. */
    private static final String KEY_ARCHIVE_FILE = "archive.file";


    /** Preferences key for the showing tooltips property. */
    private static final String KEY_SHOW_TOOLTIPS = "show.tooltips";


    /** Property: Automatic saving of the task list. */
    private boolean automaticSave;


    /** Property: Interval for automatic saving (in minutes). */
    private int automaticSaveInterval;


    /** Property: Automatic saving of the task list when application is closed. */
    private boolean saveOnClose;


    /** Property: Standard filename of the task list (opened when application starts). */
    private String standardTasklist;


    /** Property: Archive file for completed tasks. */
    private String archiveFile;


    /** Property: Show tooltips in the main window. */
    private boolean showTooltips;


    /**
     * Private constructor, initializes access to the preferences API and loads properties.
     */
    private UserProperties() {
        preferences = Preferences.userRoot().node(SimpleTaskList.class.getPackage().getName());

        /* Load preferences, use defaults if not yet available */
        automaticSave =
                preferences.getBoolean(KEY_AUTOMATIC_SAVE, DefaultProperties.AUTOMATIC_SAVE);
        automaticSaveInterval =
                preferences.getInt(KEY_AUTOMATIC_SAVE_INTERVAL, DefaultProperties.AUTOMATIC_SAVE_INTERVAL);
        saveOnClose =
                preferences.getBoolean(KEY_SAVE_ONCLOSE, DefaultProperties.SAVE_ON_CLOSE);
        standardTasklist =
                preferences.get(KEY_STANDARD_TASKLIST, DefaultProperties.STANDARD_TASKLIST);
        archiveFile =
                preferences.get(KEY_ARCHIVE_FILE, DefaultProperties.ARCHIVE_FILE);
        showTooltips =
                preferences.getBoolean(KEY_SHOW_TOOLTIPS, DefaultProperties.SHOW_TOOLTIPS);
    }



    /**
     * Getter for the only instance of UserProperties. This is the implementation of the
     * singleton pattern.
     *
     * @return The instance of UserProperties.
     */
    public static synchronized UserProperties getInstance() {
        if (properties == null) {
            properties = new UserProperties();
        }
        return properties;
    }



    /**
     * Getter for the automatic saving of the task list.
     *
     * @return The automatic saving of the task list
     */
    public boolean isAutomaticSave() {
        return automaticSave;
    }



    /**
     * Updater for the automatic saving of the task list. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param automaticSave The automatic saving of the task list
     */
    public void updateAutomaticSave(final boolean automaticSave) {
        this.automaticSave = automaticSave;
        preferences.putBoolean(KEY_AUTOMATIC_SAVE, automaticSave);
    }



    /**
     * Getter for the interval for automatic saving.
     *
     * @return The interval for automatic saving (in minutes)
     */
    public int getAutomaticSaveInterval() {
        return automaticSaveInterval;
    }



    /**
     * Updater for the interval for automatic saving. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param automaticSaveInterval The interval for automatic saving (in minutes)
     */
    public void updateAutomaticSaveInterval(final int automaticSaveInterval) {
        this.automaticSaveInterval = automaticSaveInterval;
        preferences.putInt(KEY_AUTOMATIC_SAVE_INTERVAL, automaticSaveInterval);
    }



    /**
     * Getter for the automatic saving of the task list when application is closed.
     *
     * @return The automatic saving of the task list when application is closed
     */
    public boolean isSaveOnClose() {
        return saveOnClose;
    }



    /**
     * Updater for the automatic saving of the task list when application is closed. The given
     * value is saved to the preferences and the value in this class is altered.
     *
     * @param saveOnClose Automatic saving of the task list when application is closed
     */
    public void updateSaveOnClose(final boolean saveOnClose) {
        this.saveOnClose = saveOnClose;
        preferences.putBoolean(KEY_SAVE_ONCLOSE, saveOnClose);
    }



    /**
     * Getter for the standard filename of the task list
     * (which is opened when the application starts).
     *
     * @return The standard filename of the task list
     */
    public String getStandardTasklist() {
        return standardTasklist;
    }



    /**
     * Updater for the standard filename of the task list. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param standardTasklist The standard filename of the task list
     */
    public void updateStandardTasklist(final String standardTasklist) {
        this.standardTasklist = standardTasklist;
        preferences.put(KEY_STANDARD_TASKLIST, standardTasklist);
    }



    /**
     * Getter for the archive file for completed tasks.
     *
     * @return The archive file for completed tasks
     */
    public String getArchiveFile() {
        return archiveFile;
    }



    /**
     * Updater for the archive file for completed tasks. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param archiveFile The archive file for completed tasks
     */
    public void updateArchiveFile(final String archiveFile) {
        this.archiveFile = archiveFile;
        preferences.put(KEY_ARCHIVE_FILE, archiveFile);
    }



    /**
     * Getter for showing tooltips in the main window.
     *
     * @return Showing tooltips in the main window
     */
    public boolean isShowTooltips() {
        return showTooltips;
    }



    /**
     * Updater for showing tooltips in the main window. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param showTooltips Showing tooltips in the main window
     */
    public void updateShowTooltips(final boolean showTooltips) {
        this.showTooltips = showTooltips;
        preferences.putBoolean(KEY_SHOW_TOOLTIPS, showTooltips);
    }

}
