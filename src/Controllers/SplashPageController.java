package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class to control the splashpage FXML file
 *
 * @author ??
 *
 * @version 1.0
 */
public class SplashPageController implements Initializable {

    @FXML private TextField usernameField;

    /**
     * Method to run after all FXML elements have been loaded
     *
     * @param url FXML defined resource
     * @param resourceBundle FXML defined resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Method to close current scene and open login form
     *
     * @param actionEvent Takes in the event which cause this method to be called
     *
     * @throws IOException Throws an IOException whenever there is a chance a file is not present
     */
    public void logInPage(ActionEvent actionEvent) throws IOException {
        Parent logIn = FXMLLoader.load(getClass().getResource("../FXML/LoginPage.fxml"));

        Scene loginScene = new Scene(logIn);

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setMinWidth(450);
        stage.setMinHeight(450);
        stage.setMaxWidth(650);
        stage.setMaxHeight(500);

        stage.setTitle("ProActive");

        stage.setScene(loginScene);

    }

    /**
     * Method to close current scene and open registration form
     *
     * @param actionEvent Takes in the event which cause this method to be called
     *
     * @throws IOException Throws an IOException whenever there is a chance a file is not present
     */
    public void signUpPage(ActionEvent actionEvent) throws IOException {
        Parent signUp = FXMLLoader.load(getClass().getResource("../FXML/RegForm.fxml"));

        Scene signUpScene = new Scene(signUp);

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setMinWidth(500);
        stage.setMinHeight(850);
        stage.setMaxWidth(650);
        stage.setMaxHeight(950);

        stage.setTitle("ProActive");

        stage.setScene(signUpScene);

    }
}
