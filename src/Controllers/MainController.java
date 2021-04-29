package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.User;

import java.io.IOException;
import java.net.URL;
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
 */
public class MainController implements Initializable {

    //Referencing elements defined in main.fxml
    @FXML private ImageView logo;
    @FXML private Button homeButton;
    @FXML private Button logActivityButton;
    @FXML private Button groupsButton;
    @FXML private Button goalsButton;
    @FXML private Button manageProfileButton;
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
        logo.setImage(new Image("src/Resources/proactive.png"));
    }

    /**
     * Method to allow data to be passed into this scene from another
     *
     * @param user Takes in a user object
     */
    public void initData(User user){
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
        loader.setLocation(getClass().getResource("/src/FXML/SummaryPage.fxml"));
        VBox vBox = loader.load();

        SummaryController summaryController = loader.getController();
        summaryController.initData(user);
        summaryController.setData();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);

        main.setCenter(scrollPane);
        toggleButtonFocus(homeButton);
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
    }

    /**
     * Method to set the group scene to the main screen when called
     *
     * @throws IOException Throws an IOException whenever it is possible a file could be missing
     */
    @FXML private void groupsScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/TODO.fxml"));

//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setContent(vBox);
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        scrollPane.setFitToWidth(true);

        main.setCenter(vBox);
        toggleButtonFocus(groupsButton);
    }

    /**
     * Method to set the goals scene to the main screen when called
     *
     * @throws IOException Throws an IOException whenever it is possible a file could be missing
     */
    @FXML private void goalsScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/TODO.fxml"));
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
        //loader.setLocation(getClass().getClassLoader().getResource("src/FXML/SummaryPage.fxml"));
        loader.setLocation(getClass().getResource("/FXML/ManageProfilePage.fxml"));
        VBox vBox = loader.load();

        ManageProfilePageController mppc = loader.getController();
        mppc.initData(user);
        main.setCenter(vBox);
        toggleButtonFocus(manageProfileButton);
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
    }
}
