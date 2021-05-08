package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;

import java.io.IOException;

/**
 * A class to control the RecoveryCodePage FXML file.
 *
 * @author Evan Clayton
 * @author Samuel Scarfe
 *
 * @version 1.0
 */
public class RecoveryCodePageController {

    @FXML protected Label recoveryCodePopUp;
    @FXML protected TextField recoveryCodeField;
    @FXML protected Label passwordFieldPopUp;
    @FXML protected TextField passwordField;
    @FXML protected Button submitButton;
    @FXML protected Button cancelButton;

    private final String PASSWORDREGEX  = "(?=.*\\w)(?=.*[!@#$%^&+='()*,./:;<>?{|}~])(?=\\S+$).{8,}";


    /**
     * Method to check for both a valid recovery code and valid new password.
     * If both are valid the user's password is updated.
     */
    public void submit() {
        if (DatabaseHandler.getInstance().checkRecoveryCode(recoveryCodeField.getText())) {
            if (passwordField.getText().matches(PASSWORDREGEX)) {
                //Sam please do the password insertion stuff here.
                //Thank you again for doing this :)
                //The method below gets you the userID which I hope will be helpful.
                //int userID = DatabaseHandler.getInstance().getUserIDFromRecoveryCode(recoveryCodeField.getText());
            }
            else {
                passwordFieldPopUp.setText("Invalid password. Please try again. ");
            }
        }
        else {
            recoveryCodePopUp.setText("This code is invalid. Please try a different code. ");
        }
    }

    /**
     * Method to cancel and send user back to the forgotten password page
     *
     * @param actionEvent This refers to the button that will cause this method to be called
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void cancel(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) cancelButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/ForgottenPasswordPage.fxml"));

        Parent splashParent = loader.load();

        Scene sceneParent = new Scene(splashParent);

        stage.setScene(sceneParent);

        SplashPageController controller = loader.getController();
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }


}
