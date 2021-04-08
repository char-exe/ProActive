package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    //Referencing elements defined in main.fxml
    @FXML private Button homeButton;
    @FXML private Button logActivityButton;
    @FXML private Button groupsButton;
    @FXML private Button goalsButton;
    @FXML private Button manageProfileButton;
    @FXML private BorderPane main;

    private Button lastUsedButton = new Button();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            homeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void homeScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/Summary.fxml"));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);

        main.setCenter(scrollPane);
        toggleButtonFocus(homeButton);
    }

    @FXML
    private void logActivityScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/LogActivity.fxml"));
        main.setCenter(vBox);
        toggleButtonFocus(logActivityButton);
    }

    @FXML
    private void groupsScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/TODO.fxml"));

//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setContent(vBox);
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        scrollPane.setFitToWidth(true);

        main.setCenter(vBox);
        toggleButtonFocus(groupsButton);
    }

    @FXML
    private void goalsScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/TODO.fxml"));
        main.setCenter(vBox);
        toggleButtonFocus(goalsButton);
    }

    @FXML
    private void manageProfileScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/TODO.fxml"));
        main.setCenter(vBox);
        toggleButtonFocus(manageProfileButton);
    }

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

    public void logActivity(ActionEvent actionEvent) {
    }

}
