package eu.ortlepp.tasklist.tools;

import eu.ortlepp.tasklist.gui.MainWindowController;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * A thread that regularly saves the task list if it contains unsaved changes.
 *
 * @author Thorsten Ortlepp
 */
public class AutoSaveThread extends Thread {

    /** Reference to the controller of the main window. */
    private final MainWindowController controller;


    /** The time to wait between the automatic savings. */
    private final int interval;


    /** Thread status: true = thread can run, false = thread is interrupted or stopped. */
    private boolean running;


    /**
     * Initializes the thread.
     *
     * @param controller A reference to the controller of the main window
     */
    public AutoSaveThread(final MainWindowController controller) {
        this.controller = controller;
        this.interval = UserProperties.getInstance().getAutomaticSaveInterval();
    }



    /**
     * Thread action, saves the task list every X minutes. Contains an infinite loop which
     * runs until the thread is interrupted.
     */
    @Override
    public void run() {
        running = true;

        /* Infinite loop */
        while (running) {

            /* Save the task list if it contains unsaved changes */
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!controller.isSaved()) {
                        controller.handleFileSave();
                    }
                }
            });

            /* Pause the thread for X minutes */
            try {
                sleep(TimeUnit.MINUTES.toMillis(interval));
            } catch (InterruptedException ex) {
                Logger.getLogger(AutoSaveThread.class.getName()).info("Thread interrupted");
            }
        }
    }



    /**
     * Interrupt the thread and stop the infinite loop.
     */
    @Override
    public void interrupt() {
        running = false;
        super.interrupt();
    }

}
