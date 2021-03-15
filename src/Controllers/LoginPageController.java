package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginPageController {
    private String username, password;

    //getters and setters for username and password
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LoginPageController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/loginPage.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading loginPage.fxml");
        }
    }


    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Label usernameError;
    @FXML private Label passwordError;


    //login method
    public void login(ActionEvent actionEvent) {
        username = usernameField.getText();
        password = passwordField.getText();

        //testing functionality of components
        System.out.println(username + " " + password);
        diplayInvalidPassword();
        displayInvalidUsername();


    }


    public void displayInvalidUsername() { usernameError.setText("Invalid Username"); }
    public void clearInvalidUsername() { usernameError.setText(""); }
    public void diplayInvalidPassword() { passwordError.setText("Invalid Password"); }
    public void clearInvalidPassword() { passwordError.setText(""); }

    //forgot password method
    public void forgotPassword(ActionEvent actionEvent) {
    }

    //forgot username method
    public void forgotUsername(ActionEvent actionEvent) {
    }
}
