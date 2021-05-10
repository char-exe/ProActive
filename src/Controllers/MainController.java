package Controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.GoalGenerator;
import sample.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.security.Key;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * A class to control the sidebar, notification bar and page switching mechanism of the app. Controls which page to
 * navigate to when a sidebar button is pressed.
 *
 * @author Charlie Jones 100234961
 *
 * @version 1.0 - Added basic sidebar with switching mechanism to change the focus between FXML pages, also made a last
 *                used button pointer to indicate to the user which page they are on, not allowing them to select the
 *                same page again.
 *          1.1 - Minor update to logic to ensure User object is set before being passed on to other controllers.
 *                Possibly a hacky solution, as method calls have been moved from initialise to initData.
 *          1.2 - Added method to display notifications.
 *          1.3 - Additional notification display methods for blinking and fading notifications
 */
public class MainController implements Initializable {

    //Referencing elements defined in main.fxml
    @FXML private ImageView logo;
    @FXML private Button homeButton;
    @FXML private Button logActivityButton;
    @FXML private Button groupsButton;
    @FXML private Button goalsButton;
    @FXML private Button manageProfileButton;
    @FXML private Button logOutButton;
    @FXML private BorderPane main;
    @FXML private Label notification;


    private User user;

    private Button lastUsedButton = new Button();

    /**
     * Method to be called once all FXML elements have been loaded, combined with initData acts as a pseudo-constructor
     *
     * @param url FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //logo.setImage(new Image("src/Resources/proactive.png"));
    }

    /**
     * Method to allow data to be passed into this scene from another
     *
     * @param user Takes in a user object
     */
    public void initData(User user){
        System.out.println(user);
        this.user = user;
    }

    /**
     * Method to manage the default scene that is loaded when the main FXML scene is opened
     *
     * @throws IOException Throws an IOException whenever there is a chance that a file can be missing
     */
    @FXML
    public void homeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource("src/FXML/SummaryPage.fxml"));
        loader.setLocation(getClass().getResource("/FXML/SummaryPage.fxml"));
        VBox vBox = loader.load();

        SummaryController summaryController = loader.getController();
        summaryController.initData(user);
        summaryController.initChartData(LocalDate.now());

        GoalGenerator gg = new GoalGenerator(user);
        user.setSystemGoals(gg.generateGoals());
        user.saveSystemGoals();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);

        main.setCenter(scrollPane);
        toggleButtonFocus(homeButton);
        showNotification("");

        // Sets application window title
        ((Stage) main.getScene().getWindow()).setTitle("ProActive");
    }

    /**
     * Method to set the log activity scene to the main screen when called
     *
     * @throws IOException Throws an IOException whenever it is possible a file could be missing
     */
    @FXML private void logActivityScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/LogActivity.fxml"));
        VBox vBox = loader.load();

        LogActivityController logActivityController = loader.getController();
        logActivityController.initData(user);
        main.setCenter(vBox);
        toggleButtonFocus(logActivityButton);
        showBlinkNotification("Now with animations");
    }

    /**
     * Method to set the group scene to the main screen when called
     *
     * @throws IOException Throws an IOException whenever it is possible a file could be missing
     */
    @FXML private void groupsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("FXML/Group.fxml"));
        VBox vBox = loader.load();
//        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/Group.fxml"));

//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setContent(vBox);
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        scrollPane.setFitToWidth(true);

        GroupController gc = loader.getController();
        gc.initData(user);
        gc.initUserGroupData();

        main.setCenter(vBox);
        toggleButtonFocus(groupsButton);
    }

    /**
     * Method to set the goals scene to the main screen when called
     *
     * @throws IOException Throws an IOException whenever it is possible a file could be missing
     */
    @FXML private void goalsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/GoalPage.FXML"));
        loader.setLocation(getClass().getResource("/FXML/GoalPage.fxml"));
        VBox vBox = loader.load();

        GoalController gc = loader.getController();
        gc.initData(user);
        gc.showSystemGoals();
        gc.showGroupGoals();
        main.setCenter(vBox);
        toggleButtonFocus(goalsButton);
    }

    /**
     * Method to set the manage profile scene to the main screen when called
     *
     * @throws IOException Throws an IOException whenever it is possible a file could be missing
     */
    @FXML private void manageProfileScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(getClass().getClassLoader().getResource("src/FXML/ManageProfilePage.fxml"));
        loader.setLocation(getClass().getResource("/FXML/ManageProfilePage.fxml"));
        VBox vBox = loader.load();

        ManageProfilePageController mppc = loader.getController();
        mppc.initData(user);
        main.setCenter(vBox);
        toggleButtonFocus(manageProfileButton);
        showFadeNotification("Ghost text");
    }

    /**
     * Method to manage the sidebar of the main scene, this will modify button colours when a scene is selected and
     * disallow buttons when they are active
     *
     * @param selectedButton Takes in a button object, this will allow for disabling/enabling
     */
    private void toggleButtonFocus(Button selectedButton){
        lastUsedButton.setDisable(false);

        //Setting temporary background colour for selected button
        lastUsedButton.setBackground(new Background(
                new BackgroundFill(Color.rgb(169,169,169), null, null)));

        lastUsedButton = selectedButton;
        selectedButton.setDisable(true);

        //Restoring original background colour for de-selected button
        selectedButton.setBackground(new Background(
                new BackgroundFill(Color.rgb(120,120,120), null, null)));

    }

    /**
     * Method to display text in the notification bar. This method is called by the notification handler.
     *
     * @param message Text to be displayed in the notification bar.
     */
    public void showNotification(String message){
        notification.setText(message);

        notification.setVisible(true);
        notification.setOpacity(1.0);
    }

    /**
     * Method to display text in the notification bar. The text blinks a few times before disappearing.
     *
     * @param message Text to be displayed.
     */
    public void showBlinkNotification(String message){
        notification.setText(message);

        notification.setVisible(true);
        notification.setOpacity(1.0);
        //Timeline where the text spends 1 second visible and 0.1 seconds not visible
        Timeline blinkTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), evt -> notification.setVisible(false)),
                new KeyFrame(Duration.seconds(0.1), evt -> notification.setVisible(true))
        );
        blinkTimeline.setCycleCount(3);
        blinkTimeline.play();

    }

    /**
     * Method to fade some text in and out on the notification bar a few times before disappearing.
     *
     * @param message Text to be displayed.
     */
    public void showFadeNotification(String message){
        notification.setText(message);

        notification.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), notification);
        //Float values are the opacity values
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(7);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    /**
     * Method to log out and send user back to the splash page
     *
     * @param actionEvent This refers to the button that will cause this method to be called
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void logOutAction(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) logOutButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/SplashPage.fxml"));

        Parent splashParent = loader.load();

        Scene sceneParent = new Scene(splashParent);

        stage.setScene(sceneParent);

        SplashPageController controller = loader.getController();
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }

}
