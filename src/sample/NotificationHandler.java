package sample;

import Controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

/**
 * A class to be instantiated in other classes to allow for easy displaying of notifications for goals.
 *
 * @author Evan Clayton 100278720
 *
 * @version 1.0 - Added the constructor and method needed for passing messages to the MainController to be displayed.
 *          1.1 - Now follows a singleton pattern and added a method for displaying goal completion notifications and
 *                sending an email at the same time.
 *          1.2 - Additional methods for using the alternate blink and fade notification types.
 */
public class NotificationHandler {

    private static final NotificationHandler INSTANCE = new NotificationHandler();



    private static MainController mainController;
    private static final EmailHandler emailHandler = EmailHandler.getInstance();
    private static Session session;

    /**
     * Constructor that creates the link to the MainController so that the showNotification method can be called.
     * Private to enforce singleton pattern
     *
     * @throws IOException
     */
    private NotificationHandler() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        try {
            Parent root = loader.load(); //had to put in a try catch to avoid throwing IOException so that singleton pattern can be used
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController controller = loader.<MainController>getController();
        this.mainController = controller;
        Properties prop = emailHandler.SetUpEmailHandler();
        this.session = emailHandler.createSession(prop);
    }

    /**
     * Static method to get the single instance of NotificationHandler
     * @return the NotificationHandler instance
     */
    public static NotificationHandler getInstance() { return INSTANCE; }

    /**
     * Method that calls the showNotification method from the MainController
     *
     * @param message The text to be displayed.
     */
    public void displayNotification(String message){
        mainController.showNotification(message);
    }

    /**
     * Method that calls the showBlinkNotification method from the MainController.
     *
     * @param message The text to be displayed.
     */
    public void displayBlinkNotification(String message){
        mainController.showBlinkNotification(message);
    }

    /**
     * Method that calls the showFadeNotification method from the MainController.
     *
     * @param message The text to be displayed.
     */
    public void displayFadeNotification(String message){
        mainController.showFadeNotification(message);
    }

    /**
     * Method that calls the showNotification method from the MainController as well as sending an email to the user
     * informing them of the goal's completion.
     *
     * @param email Email to send the message to.
     * @param goal the completed goal.
     */
    public void sendGoalNotificationWithEmail (String email, Goal goal) {
        String message = "You have completed the goal: " + goal.getMessageFragment();
        displayNotification(message);
        emailHandler.sendGoalCompletion(session, email, goal);
    }

    /**
     * Method that calls the showBlinkNotification method from the MainController as well as sending an email to the user
     * informing them of the goal's completion.
     *
     * @param email Email to send the message to.
     * @param goal the completed goal.
     */
    public void sendGoalNotificationWithEmail_Blink (String email, Goal goal) {
        String message = "You have completed the goal: " + goal.getMessageFragment();
        displayBlinkNotification(message);
        emailHandler.sendGoalCompletion(session, email, goal);
    }

    /**
     * Method that calls the showFadeNotification method from the MainController as well as sending an email to the user
     * informing them of the goal's completion.
     *
     * @param email Email to send the message to.
     * @param goal the completed goal.
     */
    public void sendGoalNotificationWithEmail_Fade (String email, Goal goal) {
        String message = "You have completed the goal: " + goal.getMessageFragment();
        displayFadeNotification(message);
        emailHandler.sendGoalCompletion(session, email, goal);
    }

    public void sendGroupEmail (String email, Goal goal, User user) {
        emailHandler.sendGroupGoalCompletion(session, email, goal, user);
    }

}
