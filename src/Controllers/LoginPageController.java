package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.User;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPageController {
    private String username, password;

    //getters and setters for username and password
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LoginPageController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/LoginPage.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading LoginPage.fxml");
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

        try{
            DatabaseHandler dh = new DatabaseHandler("jdbc:sqlite:proactive.db");
            String serverSidePass = dh.getPassFromUsername(username);
            if (password.equals(serverSidePass)){
                Stage parentScene = (Stage) usernameField.getScene().getWindow();

                User user = dh.createUserObjectFromUsername(username);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/FXML/Main.fxml"));
                Stage stage = new Stage();


                Parent homePageParent = loader.load();

                MainController main = loader.getController();

                Scene homeScene = new Scene(homePageParent);


                stage.setScene(homeScene);

                main.initData(user);

                parentScene.close();
                stage.show();

            }else {
                displayInvalidPassword();
            }
        } catch (SQLException userError) {
            displayInvalidUsername();
        } catch (IOException e1){
            System.out.println(e1.getMessage());
        }
    }


    public void displayInvalidUsername() { usernameError.setText("Invalid Username"); }
    public void clearInvalidUsername() { usernameError.setText(""); }
    public void displayInvalidPassword() { passwordError.setText("Invalid Password"); }
    public void clearInvalidPassword() { passwordError.setText(""); }

    //forgot password method
    public void forgotPassword(ActionEvent actionEvent) {

    }

    //forgot username method
    public void forgotUsername(ActionEvent actionEvent) {
    }
}
