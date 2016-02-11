package eu.ortlepp.tasklist.tools;

import java.util.prefs.Preferences;

/**
 * Access to the user defined (or default) shortcuts for the application.
 * This class uses the preferences API and is implemented as singleton.
 *
 * @author Thorsten Ortlepp
 */
public class ShortcutProperties {

    /** An object of this class itself. Needed for singleton implementation. */
    private static ShortcutProperties  properties;


    /** Access to the preferences API. */
    private final Preferences preferences;


    /** Preferences key for the opening shortcut. */
    private static final String KEY_OPEN = "shortcut.key.open";


    /** Preferences key for the saving shortcut. */
    private static final String KEY_SAVE = "shortcut.key.save";


    /** Preferences key for the creating shortcut. */
    private static final String KEY_NEW = "shortcut.key.new";


    /** Preferences key for the editing shortcut. */
    private static final String KEY_EDIT = "shortcut.key.edit";


    /** Preferences key for the completing shortcut. */
    private static final String KEY_DONE = "shortcut.key.done";


    /** Preferences key for the deleting shortcut. */
    private static final String KEY_DELETE = "shortcut.key.delete";


    /** Preferences key for the moving shortcut. */
    private static final String KEY_MOVE = "shortcut.key.move";


    /** Shortcut: Open a file. */
    private String keyOpen;


    /** Shortcut: Save the file. */
    private String keySave;


    /** Shortcut: Create a new task. */
    private String keyNew;


    /** Shortcut: Edit a task. */
    private String keyEdit;


    /** Shortcut: Complete a task. */
    private String keyDone;


    /** Shortcut: Delete a task. */
    private String keyDelete;


    /** Shortcut: Move completed tasks to the archive. */
    private String keyMove;


    /**
     * Private constructor, initializes access to the preferences API and loads properties.
     */
    private ShortcutProperties() {
        preferences = Preferences.userRoot().node(this.getClass().getName());

        /* Load preferences, use defaults if not yet available */
        keyOpen = preferences.get(KEY_OPEN, DefaultProperties.SHORTCUT_KEY_OPEN);
        keySave = preferences.get(KEY_SAVE, DefaultProperties.SHORTCUT_KEY_SAVE);
        keyNew = preferences.get(KEY_NEW, DefaultProperties.SHORTCUT_KEY_NEW);
        keyEdit = preferences.get(KEY_EDIT, DefaultProperties.SHORTCUT_KEY_EDIT);
        keyDone = preferences.get(KEY_DONE, DefaultProperties.SHORTCUT_KEY_DONE);
        keyDelete = preferences.get(KEY_DELETE, DefaultProperties.SHORTCUT_KEY_DELETE);
        keyMove = preferences.get(KEY_MOVE, DefaultProperties.SHORTCUT_KEY_MOVE);
    }


    /**
     * Getter for the only instance of ShortcutProperties. This is the implementation of the
     * singleton pattern.
     *
     * @return The instance of ShortcutProperties.
     */
    public static synchronized ShortcutProperties getInstance() {
        if (properties == null) {
            properties = new ShortcutProperties();
        }
        return properties;
    }



    /**
     * Getter for the shortcut to open a file.
     *
     * @return Shortcut to open a file
     */
    public String getKeyOpen() {
        return keyOpen;
    }



    /**
     * Updater for the shortcut to open a file. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param keyOpen Shortcut to open a file
     */
    public void updateKeyOpen(String keyOpen) {
        this.keyOpen = keyOpen;
        preferences.put(KEY_OPEN, keyOpen);
    }



    /**
     * Getter for the shortcut to save the file.
     *
     * @return Shortcut to save the file
     */
    public String getKeySave() {
        return keySave;
    }



    /**
     * Updater for the shortcut to save the file. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param keySave Shortcut to save the file
     */
    public void updateKeySave(String keySave) {
        this.keySave = keySave;
        preferences.put(KEY_SAVE, keySave);
    }



    /**
     * Getter for the shortcut to create a new task.
     *
     * @return Shortcut to create a new task
     */
    public String getKeyNew() {
        return keyNew;
    }



    /**
     * Updater for the shortcut to create a new task. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param keyNew Shortcut to create a new task
     */
    public void updateKeyNew(String keyNew) {
        this.keyNew = keyNew;
        preferences.put(KEY_NEW, keyNew);
    }



    /**
     * Getter for the shortcut to edit a task.
     *
     * @return Shortcut to edit a task
     */
    public String getKeyEdit() {
        return keyEdit;
    }



    /**
     * Updater for the shortcut to edit a task. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param keyEdit Shortcut to edit a task
     */
    public void updateKeyEdit(String keyEdit) {
        this.keyEdit = keyEdit;
        preferences.put(KEY_EDIT, keyEdit);
    }



    /**
     * Getter for the shortcut to mark a task as done.
     *
     * @return Shortcut to mark a task as done
     */
    public String getKeyDone() {
        return keyDone;
    }



    /**
     * Updater for the shortcut to mark a task as done. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param keyDone Shortcut to mark a task as done
     */
    public void updateKeyDone(String keyDone) {
        this.keyDone = keyDone;
        preferences.put(KEY_DONE, keyDone);
    }



    /**
     * Getter for the shortcut to delete a task.
     *
     * @return Shortcut to delete a task
     */
    public String getKeyDelete() {
        return keyDelete;
    }



    /**
     * Updater for the shortcut to delete a task. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param keyDelete Shortcut to delete a task
     */
    public void updateKeyDelete(String keyDelete) {
        this.keyDelete = keyDelete;
        preferences.put(KEY_DELETE, keyDelete);
    }



    /**
     * Getter for the shortcut to move completed tasks to the archive.
     *
     * @return Shortcut to open move completed tasks to the archive
     */
    public String getKeyMove() {
        return keyMove;
    }



    /**
     * Updater for the shortcut to move completed tasks to the archive. The given value
     * is saved to the preferences and the value in this class is altered.
     *
     * @param keyMove Shortcut to move completed tasks to the archive
     */
    public void updateKeyMove(String keyMove) {
        this.keyMove = keyMove;
        preferences.put(KEY_MOVE, keyMove);
    }

}
