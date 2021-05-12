package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

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
    @FXML protected PasswordField passwordField;
    @FXML protected PasswordField repeatPasswordField;
    @FXML protected Button submitButton;
    @FXML protected Button cancelButton;

    /**
     * Method to check for both a valid recovery code and valid new password.
     * If both are valid the user's password is updated.
     */
    public void submit() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (DatabaseHandler.getInstance().checkRecoveryCode(recoveryCodeField.getText())) {
            String password = passwordField.getText();

            String PASSWORDREGEX = "(?=.*\\w)(?=.*[!@#$%^&+='()*,./:;<>?{|}~])(?=\\S+$).{8,}";
            if (password.matches(PASSWORDREGEX)) {
                String repeatPassword = repeatPasswordField.getText();
                if (password.equals(repeatPassword)) {
                    int userID = DatabaseHandler.getInstance().getUserIDFromRecoveryCode(recoveryCodeField.getText());

                    //Password hashing
                    SecureRandom sr = new SecureRandom();
                    byte[] salt = new byte[16];
                    sr.nextBytes(salt);

                    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
                    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

                    byte[] hash = factory.generateSecret(spec).getEncoded();

                    DatabaseHandler.getInstance().updatePassword(userID, hash, salt);


                    passwordFieldPopUp.setText("Password reset, return to login to proceed");
                }
                else {
                    passwordFieldPopUp.setText("Passwords do not match");
                }
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
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void cancel() throws IOException {
        Stage parentScene = (Stage) cancelButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/ForgottenPasswordPage.fxml"));

        Parent splashParent = loader.load();

        Scene sceneParent = new Scene(splashParent);

        stage.setScene(sceneParent);

        ForgottenPasswordPageController controller = loader.getController();
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }
}
