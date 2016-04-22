package eu.ortlepp.tasklist.model;

/**
 * Data object for position, width and height of a window.
 *
 * @author Thorsten Ortlepp
 */
public class ParentWindowData {

    /** The x position of the window. */
    private final double posX;


    /** The y position of the window. */
    private final double posY;


    /** The width of the window. */
    private final double width;


    /** The height of the window. */
    private final double height;


    /**
     * Initialize the data object with the given values.
     *
     * @param posX The current x position of the parent window
     * @param posY The current y position of the parent window
     * @param width The current width of the parent window
     * @param height The current height of the parent window
     */
    public ParentWindowData(final double posX, final double posY, final double width, final double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }



    /**
     * Getter for the x position of the window.
     *
     * @return The x position of the window
     */
    public double getX() {
        return posX;
    }



    /**
     * Getter for the y position of the window.
     *
     * @return The y position of the window
     */
    public double getY() {
        return posY;
    }



    /**
     * Getter for the width of the window.
     *
     * @return The width of the window
     */
    public double getWidth() {
        return width;
    }



    /**
     * Getter for the height of the window.
     *
     * @return The height of the window
     */
    public double getHeight() {
        return height;
    }

}
