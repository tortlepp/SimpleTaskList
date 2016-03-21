package eu.ortlepp.tasklist.gui;

import eu.ortlepp.tasklist.model.ParentWindowData;
import javafx.stage.Stage;

/**
 * Extension for the original Stage. Offers a method to place the stage (a dialog) in the center
 * of its parent.
 *
 * @author Thorsten Ortlepp
 */
public class DialogStage extends Stage {

    /**
     * Set the position of the stage. The stage is placed in the center of its parent window.
     *
     * @param windowData The position and size of the parent window
     */
    public void setPosition(final ParentWindowData windowData) {
        this.setX(windowData.getX() + (windowData.getWidth() / 2) - (this.getWidth() / 2));
        this.setY(windowData.getY() + (windowData.getHeight() / 2) - (this.getHeight() / 2));
    }

}
