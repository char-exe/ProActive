package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.Arrays;

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

            //Retrieve user's hashed password and salt value
            byte[] serverSidePass = dh.getHashFromUsername(username);
            byte[] salt = dh.getSaltFromUsername(username);

            //Hash inputted password
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            //Compare hashed input to stored hash
            if (Arrays.equals(hash, serverSidePass)){
                Parent homePage = FXMLLoader.load(getClass().getResource("../FXML/Main.fxml"));

                Scene homeScene = new Scene(homePage);

                Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

                window.setScene(homeScene);
            }else {
                displayInvalidPassword();
            }
        } catch (SQLException userError) {
            displayInvalidUsername();
        } catch (IOException e1){
            System.out.println(e1.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("String passed to SecretKeyFactory.getInstance has been spelled incorrectly, or is" +
                    "otherwise incorrect.");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
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
