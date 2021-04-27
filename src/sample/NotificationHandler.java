package sample;

import Controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * A class to be instantiated in other classes to allow for easy displaying of notifications for goals.
 *
 * @author Evan Clayton 100278720
 *
 * @version 1.0 Added the constructor and method needed for passing messages to the MainController to be displayed.
 */
public class NotificationHandler {

    private MainController mainController;

    /**
     * Constructor that creates the link to the MainController so that the showNotification method can be called.
     *
     * @throws IOException
     */
    public NotificationHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.<MainController>getController();
        this.mainController = controller;
    }

    /**
     * Method that calls the showNotification method from the MainController
     *
     * @param message The text to be displayed.
     */
    public void displayNotification(String message){
        mainController.showNotification(message);
    }
}
