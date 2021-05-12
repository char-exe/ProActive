package sample;

import Controllers.MainController;

import javax.mail.Session;

/**
 * A class to be instantiated in other classes to allow for easy displaying of notifications for goals.
 *
 * @author Evan Clayton 100278720
 * @author Samuel Scarfe
 *
 * @version 1.0 - Added the constructor and method needed for passing messages to the MainController to be displayed.
 *          1.1 - Now follows a singleton pattern and added a method for displaying goal completion notifications and
 *                sending an email at the same time.
 *          1.2 - Additional methods for using the alternate blink and fade notification types.
 *          1.3 - Removed unused methods.
 */
public class NotificationHandler {

    private static final NotificationHandler INSTANCE = new NotificationHandler();

    private static MainController mainController;

    /**
     * Constructor that creates the link to the MainController so that the showNotification method can be called.
     * Private to enforce singleton pattern
     */
    private NotificationHandler() {
    }

    /**
     * Static method to get the single instance of NotificationHandler
     * @return the NotificationHandler instance
     */
    public static NotificationHandler getInstance() {
        return INSTANCE;
    }

    public void initMainController(MainController mainController) {
        NotificationHandler.mainController = mainController;
    }

    /**
     * Method that calls the showFadeNotification method from the MainController.
     *
     * @param message The text to be displayed.
     */
    public void displayFadeNotification(String message) {
        mainController.showFadeNotification(message);
    }
}
