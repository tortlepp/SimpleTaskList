package eu.ortlepp.tasklist;

import eu.ortlepp.tasklist.gui.MainWindowController;
import eu.ortlepp.tasklist.tools.AutoSaveThread;
import eu.ortlepp.tasklist.tools.DefaultProperties;
import eu.ortlepp.tasklist.tools.UserProperties;
import eu.ortlepp.tasklist.tools.WindowProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Main class for the application, contains the main method.
 * Starts the application and loads the main window.
 *
 * @author Thorsten Ortlepp
 */
public final class SimpleTaskList extends Application {

    /** ResourceBundle with translated captions, tooltips and messages. */
    public static final String TRANSLATION = "eu.ortlepp.tasklist.i18n.tasklist";


    /** The current version of the program. */
    public static final String VERSION = "Version 1.1";


    /** The stage for the window. Contains all components of the window. */
    private Stage primaryStage;


    /** The controller of the main window. */
    private MainWindowController controller;


    /** Settings for the main window. */
    private WindowProperties properties;


    /** Thread to save the current task list automatically. */
    private AutoSaveThread autosave;


    /**
     * The main method to start the application and launch the main window.
     *
     * @param args Parameters for the application, currently not used
     */
    public static void main(final String... args) {
        launch(args);
    }



    /**
     * Initialize the main window. The title and icon of the window is set.
     * The window is designed to be resizable but has a minimum size of 700x500.
     *
     * @param primaryStage The stage for the window, contains all components of the window
     */
    @Override
    public void start(final Stage primaryStage) {
        /* Initialize Logging */
        try {
            LogManager.getLogManager().readConfiguration(this.getClass().getClassLoader()
                    .getResourceAsStream("eu/ortlepp/tasklist/properties/logging.properties"));
        } catch (SecurityException | IOException ex) {
            Logger.getLogger(SimpleTaskList.class.getName())
                    .severe("Initialization of the logger failed: " + ex.getMessage());
        }

        /* Load properties */
        properties = new WindowProperties();

        /* Initialize main window */
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(true);
        this.primaryStage.getIcons().add(new Image("eu/ortlepp/tasklist/icons/SimpleTaskList-16.png"));
        this.primaryStage.getIcons().add(new Image("eu/ortlepp/tasklist/icons/SimpleTaskList-32.png"));
        this.primaryStage.getIcons().add(new Image("eu/ortlepp/tasklist/icons/SimpleTaskList-48.png"));
        this.primaryStage.getIcons().add(new Image("eu/ortlepp/tasklist/icons/SimpleTaskList-64.png"));
        this.primaryStage.setMaximized(properties.isMaximizedWindow());
        this.primaryStage.setWidth(properties.getWidth());
        this.primaryStage.setHeight(properties.getHeight());
        this.primaryStage.setMinWidth(DefaultProperties.WINDOW_WIDTH);
        this.primaryStage.setMinHeight(DefaultProperties.WINDOW_HEIGHT);

        /* Set the position of the window if it is not the default position */
        if (properties.getPosX() != DefaultProperties.WINDOW_POS_X
                && properties.getPosY() != DefaultProperties.WINDOW_POS_Y) {
            this.primaryStage.setX((int) properties.getPosX());
            this.primaryStage.setY((int) properties.getPosY());
        }

        initWindow();
    }


    /**
     * Do further initialization of the main window. Load the language specific captions
     * and the FXML file. Initializes the controller as well.
     */
    private void initWindow() {
        try {
            /* Load the translation for the GUI */
            final ResourceBundle bundle = PropertyResourceBundle.getBundle(TRANSLATION);

            /* Set the title of the main window */
            this.primaryStage.setTitle(bundle.getString("app.title"));

            /* Load FXML and initialize the main window */
            final FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("eu/ortlepp/tasklist/fxml/MainWindow.fxml"), bundle);
            final BorderPane window = (BorderPane) loader.load();

            /* Get the file which was given as a parameter */
            final List<String> params = getParameters().getRaw();
            String file = params.isEmpty() ? "" : params.get(0);

            /* If no file was given as parameter check the settings for a file to open */
            if (file.isEmpty()) {
                file = UserProperties.getInstance().getStandardTasklist();
            }

            /* Initialize the controller, pass file to open to the controller */
            controller = loader.getController();
            controller.setStage(primaryStage);
            controller.loadTaskList(file);

            /* If automatic saving is enabled the thread needs to be started */
            if (UserProperties.getInstance().isAutomaticSave()) {
                autosave = new AutoSaveThread(controller);
                autosave.start();
            }

            /* Show the window */
            final Scene scene = new Scene(window);
            primaryStage.setScene(scene);
            primaryStage.show();

            /* Initialize keyboard shortcuts */
            controller.initShortcuts();
        } catch (IOException ex) {
            Logger.getLogger(SimpleTaskList.class.getName())
                    .severe("Initialization of the main window failed: " + ex.getMessage());
        }
    }



    /**
     * Stop the application. If enabled the task list is automatically saved.
     */
    @Override
    public void stop() throws Exception {
        /* Abort the automatic saving thread */
        if (autosave != null) {
            autosave.interrupt();
        }

        /* Automatic save if enabled or ask if file is not yet saved */
        if (UserProperties.getInstance().isSaveOnClose()) {
            controller.handleFileSave();
        } else {
            if (!controller.isSaved()) {
                controller.confirmSave();
            }
        }

        /* Save properties of the main window */
        properties.updateMaximizedWindow(this.primaryStage.isMaximized());
        if (!this.primaryStage.isMaximized()) {
            properties.updateWidth((int) this.primaryStage.getWidth());
            properties.updateHeight((int) this.primaryStage.getHeight());
            properties.updatePosX((int) this.primaryStage.getX());
            properties.updatePosY((int) this.primaryStage.getY());
        }

        /* Finally close the application */
        super.stop();
    }

}
