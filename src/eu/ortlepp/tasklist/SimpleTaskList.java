package eu.ortlepp.tasklist;

import java.io.IOException;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import eu.ortlepp.tasklist.gui.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main class for the application, contains the main method. Starts the application and loads the main window.
 *
 * @author Thorsten Ortlepp
 */
public final class SimpleTaskList extends Application {


    /** ResourceBundle with translated captions, tooltips and messages. */
    public static final String TRANSLATION = "eu.ortlepp.tasklist.i18n.tasklist";


    /** The stage for the window. Contains all components of the window. */
    private Stage primaryStage;


    /**
     * The main method to start the application and launch the main window.
     *
     * @param args Parameters for the application, currently not used
     */
    public static void main(String... args) {
        launch(args);
    }



    /**
     * Initialize the main window. The title and icon of the window is set. The window is designed to be resizable but has a miniumu sixe of 700x500.
     *
     * @param primaryStage The stage for the window, contains all components of the window
     */
    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SimpleTaskList");
        this.primaryStage.setResizable(true);
        this.primaryStage.setMinWidth(800);
        this.primaryStage.setMinHeight(600);
        initWindow();
    }


    /**
     * Do further initialization of the main window. Load the language specific captions and the FXML file.
     * Initializes the controller as well.
     */
    private void initWindow() {
        try {
            /* Load the translation for the GUI */
            ResourceBundle bundle = PropertyResourceBundle.getBundle(TRANSLATION);

            /* Load FXML and initialize the main window */
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("eu/ortlepp/tasklist/fxml/mainwindow.fxml"), bundle);
            BorderPane window = (BorderPane) loader.load();

            /* Initialize the controller, pass file to open to the controller */
            List<String> params = getParameters().getRaw();
            String file = (params.size() > 0) ? params.get(0) : "";
            MainWindowController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.loadTaskList(file);

            /* Show the window */
            Scene scene = new Scene(window);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            System.err.println("Initialization of the main window failed: " + ex.getMessage());
        }
    }

}
