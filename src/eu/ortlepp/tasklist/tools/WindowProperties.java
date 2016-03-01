package eu.ortlepp.tasklist.tools;

import java.util.prefs.Preferences;

import eu.ortlepp.tasklist.SimpleTaskList;

/**
 * Access window settings for the main window of the application.
 * This class uses the preferences API.
 *
 * @author Thorsten Ortlepp
 */
public class WindowProperties {

    /** Access to the preferences API. */
    private final Preferences preferences;


    /** Preferences key for the maximization property. */
    private static final String KEY_WINDOW_MAXIMIZED = "window.maximized";


    /** Preferences key for the width property. */
    private static final String KEY_WINDOW_WIDTH = "window.width";


    /** Preferences key for the height property. */
    private static final String KEY_WINDOW_HEIGHT = "window.height";


    /** Preferences key for the x position property. */
    private static final String KEY_WINDOW_POS_X = "window.pos.x";


    /** Preferences key for the y position property. */
    private static final String KEY_WINDOW_POS_Y = "window.pos.y";


    /** Property: Maximize the main window. */
    private boolean maximizeWindow;


    /** Property: Width of the main window. */
    private int width;


    /** Property: Height of the main window. */
    private int height;


    /** Property: X position of the main window. */
    private int posX;


    /** Property: Y position of the main window. */
    private int posY;


    /**
     * Constructor, initializes access to the preferences API and loads properties.
     */
    public WindowProperties() {
        preferences = Preferences.userRoot().node(SimpleTaskList.class.getPackage().getName());

        /* Load preferences, use defaults if not yet available */
        maximizeWindow =
                preferences.getBoolean(KEY_WINDOW_MAXIMIZED, DefaultProperties.WINDOW_MAXIMIZED);
        width = preferences.getInt(KEY_WINDOW_WIDTH, DefaultProperties.WINDOW_WIDTH);
        height = preferences.getInt(KEY_WINDOW_HEIGHT, DefaultProperties.WINDOW_HEIGHT);
        posX = preferences.getInt(KEY_WINDOW_POS_X, DefaultProperties.WINDOW_POS_X);
        posY = preferences.getInt(KEY_WINDOW_POS_Y, DefaultProperties.WINDOW_POS_Y);
    }



    /**
     * Getter for the maximization of the main window.
     *
     * @return Maximization of the main window
     */
    public boolean isMaximizedWindow() {
        return maximizeWindow;
    }



    /**
     * Updater for the maximization of the main window. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param maximizeWindow Maximization of the main window
     */
    public void updateMaximizedWindow(boolean maximizeWindow) {
        this.maximizeWindow = maximizeWindow;
        preferences.putBoolean(KEY_WINDOW_MAXIMIZED, maximizeWindow);
    }



    /**
     * Getter for the width of the main window.
     *
     * @return The width of the main window
     */
    public int getWidth() {
        return width;
    }



    /**
     * Updater for the width of the main window. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param width The width of the main window (in pixels)
     */
    public void updateWidth(int width) {
        this.width = width;
        preferences.putInt(KEY_WINDOW_WIDTH, width);
    }



    /**
     * Getter for the height of the main window.
     *
     * @return The height of the main window
     */
    public int getHeight() {
        return height;
    }



    /**
     * Updater for the height of the main window. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param height The height of the main window (in pixels)
     */
    public void updateHeight(int height) {
        this.height = height;
        preferences.putInt(KEY_WINDOW_HEIGHT, height);
    }



    /**
     * Getter for the x position of the main window.
     *
     * @return The x position of the main window
     */
    public int getPosX() {
        return posX;
    }



    /**
     * Updater for the x position of the main window. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param posX The x position of the main window (in pixels)
     */
    public void updatePosX(int posX) {
        this.posX = posX;
        preferences.putInt(KEY_WINDOW_POS_X, posX);
    }



    /**
     * Getter for the y position of the main window.
     *
     * @return The y position of the main window
     */
    public int getPosY() {
        return posY;
    }



    /**
     * Updater for the y position of the main window. The given value is saved to the
     * preferences and the value in this class is altered.
     *
     * @param posY The y position of the main window (in pixels)
     */
    public void updatePosY(int posY) {
        this.posY = posY;
        preferences.putInt(KEY_WINDOW_POS_Y, posY);
    }

}
