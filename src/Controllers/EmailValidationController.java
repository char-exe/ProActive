package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EmailValidationController {
//This is probably better off just as the email handler class and linking emailvalidation.fxml straight to it.
    @FXML public Label header;

    @FXML public Button submitButton;

    @FXML protected void initialize(){
        header.setText("Please check your emails for a validation code.");
    }

    @FXML protected void SubmitClicked(){
        //confirm with email handler the code is correct.
    }

    @FXML protected void ResendVerification(){
        //Get email handler to resend email
    }
}
