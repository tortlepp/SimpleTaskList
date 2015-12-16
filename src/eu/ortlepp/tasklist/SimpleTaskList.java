package eu.ortlepp.tasklist;

import java.io.IOException;

import eu.ortlepp.tasklist.gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimpleTaskList extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SimpleTaskList");
        this.primaryStage.setResizable(false);
        initWindow();
    }


    private void initWindow() {
        try {
            /* Load FXML of the main window */
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SimpleTaskList.class.getResource("gui/Window.fxml"));
            BorderPane window = (BorderPane) loader.load();

            /* Initialize the controller */
            Controller controller = loader.getController();

            /* Show the window */
            Scene scene = new Scene(window);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            System.err.println("Initialization of the main window failed: " + ex.getMessage());
        }
    }

}
