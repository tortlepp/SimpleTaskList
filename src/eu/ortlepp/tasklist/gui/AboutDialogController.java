package eu.ortlepp.tasklist.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import eu.ortlepp.tasklist.SimpleTaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller for the about dialog window. Handles all actions of the dialog window.
 *
 * @author Thorsten Ortlepp
 */
public class AboutDialogController extends AbstractDialogController {

    /** A logger to write out events and messages to the console. */
    private static final Logger LOGGER = Logger.getLogger(AboutDialogController.class.getName());


    /** URL of the project page. */
    private static final String URL_PROJECT = "https://github.com/tortlepp/SimpleTaskList";


    /** URL to the license. */
    private static final String URL_LICENSE = "https://opensource.org/licenses/MIT";


    /** URL to the icon source. */
    private static final String URL_ICONS = "http://fontawesome.io";


    /** The image area to display the program icon. */
    @FXML
    private ImageView imgvLogo;


    /** The label to display the current program version. */
    @FXML
    private Label lblVersion;


    /** The clickable URL of the project page shown in the GUI. */
    @FXML
    private Hyperlink urlProject;


    /** The clickable URL to the license shown in the GUI. */
    @FXML
    private Hyperlink urlLicense;


    /** The clickable URL to the icon source shown in the GUI. */
    @FXML
    private Hyperlink urlIcons;



    /**
     * Initialize the dialog and its components.
     */
    @FXML
    @Override
    protected void initialize() {
        imgvLogo.setImage(new Image("eu/ortlepp/tasklist/icons/SimpleTaskList.png"));
        lblVersion.setText(SimpleTaskList.VERSION);
        urlProject.setText(URL_PROJECT);
        urlLicense.setText(URL_LICENSE);
        urlIcons.setText(URL_ICONS);

    }



    /**
     * Open the URL of the project page in the default browser.
     */
    @FXML
    private void handleUrlOpenProject() {
        openBrowser(URL_PROJECT);
    }



    /**
     * Open the URL to the license in the default browser.
     */
    @FXML
    private void handleUrlOpenLicense() {
        openBrowser(URL_LICENSE);
    }



    /**
     * Open the URL to the icon source in the default browser.
     */
    @FXML
    private void handleUrlOpenIcons() {
        openBrowser(URL_ICONS);
    }



    /**
     * Open an URL in the default browser.
     *
     * @param url The URL to open
     */
    private void openBrowser(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException ex) {
            LOGGER.severe("Opening URL failed: " + ex.getMessage());
        }
    }

}
