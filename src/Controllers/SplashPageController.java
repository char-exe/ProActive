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

public class SplashPageController implements Initializable {
    
    @FXML private TextField usernameField;
    @FXML private void doAction (ActionEvent event) {
        System.out.println(usernameField.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //method for navigating to login page
    public void logInPage(ActionEvent actionEvent) throws IOException {
        Parent logIn = FXMLLoader.load(getClass().getResource("../FXML/LoginPage.fxml"));

        Scene loginScene = new Scene(logIn, 1400, 800);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(loginScene);

    }

    //method for navigating to signup page
    public void signUpPage(ActionEvent actionEvent) throws IOException {
        Parent signUp = FXMLLoader.load(getClass().getResource("../FXML/RegForm.fxml"));

        Scene signUpScene = new Scene(signUp, 1400, 800);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(signUpScene);

    }
}
