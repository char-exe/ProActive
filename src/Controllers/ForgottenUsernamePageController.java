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

    private final String EMAILREGEX     = "^\\w+.?\\w+@\\w+[.]\\w+([.]\\w+){0,2}$";

    @FXML
    protected void submit() throws IOException {
        if (emailField.getText().matches(EMAILREGEX)) {
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
     * @param actionEvent This refers to the button that will cause this method to be called
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void cancel(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) cancelButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/SplashPage.fxml"));

        Parent splashParent = loader.load();

        Scene sceneParent = new Scene(splashParent);

        stage.setScene(sceneParent);

        SplashPageController controller = loader.getController();
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }

}
