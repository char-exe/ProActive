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

    public SplashPageController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/splashPage.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading splashPage.fxml");
        }
    }
    
    @FXML private TextField usernameField;
    @FXML private void doAction (ActionEvent event) {
        System.out.println(usernameField.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/splashPage.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading splashPage.fxml");
        }
    }

    //method for navigating to login page
    public void logInPage(ActionEvent actionEvent) throws IOException {
        Parent logIn = FXMLLoader.load(getClass().getResource("../FXML/loginPage.fxml"));

        Scene loginScene = new Scene(logIn);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(loginScene);

    }

    //method for navigating to signup page
    public void signUpPage(ActionEvent actionEvent) throws IOException {
        Parent signUp = FXMLLoader.load(getClass().getResource("../FXML/RegForm.fxml"));

        Scene signUpScene = new Scene(signUp);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(signUpScene);

    }
}
