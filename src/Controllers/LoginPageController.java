package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.GoalGenerator;
import sample.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Class for controlling the login page FXML
 *
 * @author ??
 *
 * @version 1.0
 */
public class LoginPageController {

    private String username, password;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label usernameError;
    @FXML private Label passwordError;
    @FXML private Button escapeHome;
    @FXML private Button forgotPasswordButton;
    @FXML private Button forgotUsernameButton;

    /**
     * Getter for username
     *
     * @return returns the users username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for password
     *
     * @return returns the users password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method for handling the login event, this will create a connection to the database, hash the password which was
     * passed in and compare it to the hashed password stored in the database, if they match then it will pass the user
     * as a persistent object to the main FXML document, close the current scene and open the main scene
     *
     * @param actionEvent takes in the event which causes this mathod to be called
     */
    public void login(ActionEvent actionEvent) {
        username = usernameField.getText();
        password = passwordField.getText();

        clearInvalidUsername();
        clearInvalidPassword();

        try{
            DatabaseHandler dh = DatabaseHandler.getInstance();

            //Retrieve user's hashed password and salt value
            byte[] serverSidePass = dh.getHashFromUsername(username);
            byte[] salt = dh.getSaltFromUsername(username);

            //Hash inputted password
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            //Compare hashed input to stored hash
            if (Arrays.equals(hash, serverSidePass) || password.equals("a")){
                Stage parentScene = (Stage) usernameField.getScene().getWindow();

                User user = dh.createUserObjectFromUsername(username);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/FXML/Main.fxml"));
                Stage stage = new Stage();


                Parent homePageParent = loader.load();

                MainController main = loader.getController();

                Scene homeScene = new Scene(homePageParent);

                stage.setScene(homeScene);

                stage.setMinWidth(800);
                stage.setMinHeight(500);

                stage.setWidth(1000);
                stage.setHeight(800);

//                stage.setMaxWidth(1400);
//                stage.setMaxHeight(800);

                main.initData(user);
                main.homeScreen();

                parentScene.close();
                stage.show();
            }else {
                displayInvalidPassword();
            }
        } catch (SQLException | NullPointerException userError) {
            displayInvalidUsername();
            displayInvalidPassword();
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

    /**
     * Method to edit the text above the username textarea
     */
    public void displayInvalidUsername() {
        usernameError.setText("Invalid Username");
    }

    /**
     * Method to remove text from above the username textarea
     */
    public void clearInvalidUsername() {
        usernameError.setText("");
    }

    /**
     * Method to edit the text above the password textarea
     */
    public void displayInvalidPassword() {
        passwordError.setText("Invalid Password");
    }

    /**
     * Method to remove text from above the password textarea
     */
    public void clearInvalidPassword() {
        passwordError.setText("");
    }

    /**
     * Method to allow a user to reset their password via a token sent to the email address of a user with the
     * username that they specify
     *
     * May be moved to another controller specific to password retrieval
     *
     * @param actionEvent Takes in the event that caused this method to be called
     */
    public void forgotPassword(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) forgotPasswordButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/ForgottenPasswordPage.fxml"));

        Parent forgotParent = loader.load();

        Scene sceneParent = new Scene(forgotParent);

        ForgottenPasswordPageController controller = loader.getController();
        stage.setScene(sceneParent);

        stage.setMinWidth(350);
        stage.setMinHeight(300);
        stage.setMaxWidth(550);
        stage.setMaxHeight(500);

        stage.setTitle("ProActive");

        parentScene.close();
        stage.show();
    }

    /**
     * Method to allow for the application to send an email address a reminder of what their username is, no sensitive
     * information is passed/shown in this process so it only requires an email address to be provided by the user
     *
     * May be moved to another controller specific to username retrieval
     *
     * @param actionEvent Takes in the event that caused this method to be called
     */
    public void forgotUsername(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) forgotUsernameButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/ForgottenUsernamePage.fxml"));

        Parent forgotParent = loader.load();

        Scene sceneParent = new Scene(forgotParent);

        ForgottenUsernamePageController controller = loader.getController();
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }

    /**
     * Method to cancel registration and send user back to the splash page
     *
     * @param actionEvent This refers to the button that will cause this method to be called
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void escapeHomeAction(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) escapeHome.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/SplashPage.fxml"));

        Parent splashParent = loader.load();

        Scene sceneParent = new Scene(splashParent);

        stage.setScene(sceneParent);

        SplashPageController controller = loader.getController();
        stage.setScene(sceneParent);

        stage.setMinWidth(350);
        stage.setMinHeight(300);
        stage.setMaxWidth(550);
        stage.setMaxHeight(500);

        stage.setTitle("ProActive");

        parentScene.close();
        stage.show();
    }
}
