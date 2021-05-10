package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.EmailHandler;

import java.io.IOException;

/**
 * A class to control the ForgottenPasswordPage FXML file.
 *
 * @author Evan Clayton
 *
 * @version 1.0
 */

public class ForgottenUsernamePageController {

    @FXML protected Label emailFieldPopUp;
    @FXML protected TextField emailField;
    @FXML protected Button submitButton;
    @FXML protected Button cancelButton;

    @FXML
    protected void submit() throws IOException {
        String EMAILREGEX = "^\\w+.?\\w+@\\w+[.]\\w+([.]\\w+){0,2}$";
        if (emailField.getText().matches(EMAILREGEX) && DatabaseHandler.getInstance().checkEmailUnique(emailField.getText())) {
            //send the email with the code
            EmailHandler.getInstance().sendUsernameRecoveryEmailCSS(EmailHandler.getInstance().createSession(), emailField.getText());

            emailFieldPopUp.setText("Your Username has been emailed to the address provided");

        }
        else {
            emailFieldPopUp.setText("Invalid email, please try again.");
        }
    }

    /**
     * Method to cancel and send user back to the splash page
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void cancel() throws IOException {
        Stage parentScene = (Stage) cancelButton.getScene().getWindow();
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
