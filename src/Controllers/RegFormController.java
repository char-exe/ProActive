package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;

import java.sql.SQLOutput;

public class RegFormController {

    @FXML public Label emailPopUp;
    @FXML public Label passwordPopUp;
    @FXML public Label repeatPasswordPopUp;


    @FXML public TextField firstNameField;
    @FXML public TextField lastNameField;
    @FXML public TextField emailField;
    @FXML public TextField usernameField;

    @FXML public PasswordField passwordField;
    @FXML public PasswordField repeatPasswordField;

    @FXML public Hyperlink termsConsLink;
    @FXML public CheckBox termsCheckBox;

    @FXML public Button submitButton;


    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        if (
                CheckRepeatPassword() &&
                CheckTermsBox()
        ){
            //checks inputs^ then double checks with user class
            //validate user inputs with user class
            //if failed then need to know what will be bad for user
            //java scenes
            //call captcha
            //validate email with email handler
            //need a resend option
            //wait on email handler to carry on
            //tell database handler to create user with the details
            //end

            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            System.out.println("First name = " + firstName);
            System.out.println("Last Name = " + lastName);
            System.out.println("Email = " + email);
            System.out.println("Username = " + username);
            System.out.println("Password = " + password);
        }
        else{
            submitButton.setTextFill(Paint.valueOf("#bf2323"));
        }
    }
    @FXML protected boolean CheckName(){
        //check is all chars and suitable length
        return true;
    }

    @FXML protected boolean CheckEmail(){
        //needs to check for email ragax
        String email = emailField.getText();
        String pattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}";
        return true;
    }

    @FXML protected boolean CheckPassword(){

        if (repeatPasswordField.getText().length() != 0){CheckRepeatPassword();}

        String password = passwordField.getText();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        if(password.matches(pattern)){
            passwordPopUp.setText("");
            return true;
        }
        else{
            passwordPopUp.setText("Needs to include minimum of 8 Characters, 1 Digit and 1 Special character");
            return false;
        }
    }

    @FXML protected boolean CheckRepeatPassword(){
        if (!passwordField.getText().equals(repeatPasswordField.getText())){
            repeatPasswordPopUp.setText("Passwords do not match.");
            submitButton.setTextFill(Paint.valueOf("darkred"));
            return false;
        }
        else{
            submitButton.setTextFill(Paint.valueOf("black"));
            repeatPasswordPopUp.setText("");
            return true;
        }
    }
    @FXML protected boolean CheckTermsBox(){
        if (termsCheckBox.isSelected()&&!termsConsLink.getTextFill().equals(Paint.valueOf("darkred"))){
            return true;
        }
        else if(termsCheckBox.isSelected()&&termsConsLink.getTextFill().equals(Paint.valueOf("darkred"))){
            termsConsLink.setTextFill(Paint.valueOf("black"));
            submitButton.setTextFill(Paint.valueOf("black"));
            return true;
        }
        else{
            termsConsLink.setTextFill(Paint.valueOf("darkred"));
            submitButton.setTextFill(Paint.valueOf("darkred"));
            return false;
        }
    }
}
