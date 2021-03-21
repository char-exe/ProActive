package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.EmailHandler;
import sample.TokenHandler;
import sample.User;

import javax.mail.Session;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


public class RegFormController implements Initializable {

    //Regexes for checking components
    private final String FIRSTNAMEREGEX = "[A-Z]?[a-z]*";
    private final String LASTNAMEREGEX  = "[a-zA-Z]*([ '-][a-zA-Z]+)*";
    private final String EMAILREGEX     = "^\\w+.?\\w+@\\w+[.]\\w+([.]\\w+){0,2}$";
    private final String USERNAMEREGEX  = "^[a-z0-9_-]{5,16}$";
    private final String PASSWORDREGEX  = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";


    @FXML public Label firstNamePopUp;
    @FXML public Label lastNamePopUp;
    @FXML public Label emailPopUp;
    @FXML public Label usernamePopUp;
    @FXML public Label sexPopUp;
    @FXML public Label passwordPopUp;
    @FXML public Label repeatPasswordPopUp;
    @FXML public Label dateOfBirthPopUp;

    @FXML public TextField firstNameField;
    @FXML public TextField lastNameField;
    @FXML public TextField emailField;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField repeatPasswordField;
    @FXML public Hyperlink termsConsLink;
    @FXML public CheckBox termsCheckBox;
    @FXML public Button submitButton;
    @FXML public ChoiceBox sexCombo;
    @FXML public DatePicker dateField;
    @FXML public TextField heightField;
    @FXML public TextField weightField;
    @FXML public Label heightPopUp;
    @FXML public Label weightPopUp;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws IOException {

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws IOException {

        if (
                CheckFirstName() &&
                CheckLastName() &&
                CheckEmail() &&
                CheckPassword() &&
                CheckRepeatPassword() &&
                CheckTermsBox()
        ){
            //checks username with database handler for uniqueness.
            //checks inputs^ then double checks with user class
            //validate user inputs with user class
            //java scenes
            //call captcha
            //validate email with email handler
            //need a resend option
            //wait on email handler to carry on
            //tell database handler to create user with the details
            //end

            //Instantiate Database and Email handlers
            DatabaseHandler db = new DatabaseHandler("jdbc:sqlite:proactive.db");
            EmailHandler eh = new EmailHandler("proactivese13@gmail.com", "f45d09mFAcHr");
            Properties prop = eh.SetUpEmailHandler();
            Session session = eh.createSession(prop);

            //Read in all of the information from the registration form
            String firstName    = firstNameField.getText();
            String lastName     = lastNameField.getText();
            String email        = emailField.getText();
            LocalDate dob       = dateField.getValue();
            float height        = Float.parseFloat(heightField.getText());
            float weight        = Float.parseFloat(weightField.getText());
            String username     = usernameField.getText();
            String sex          = sexCombo.getValue().toString();
            String password     = passwordField.getText();


            if(!db.checkUserNameUnique(username)){
                usernamePopUp.setText("Username Not Unique, Please Select a new one");
            }else {

                //Load Captcha, wait for it to complete then move on
                Parent captcha = FXMLLoader.load(getClass().getResource("/FXML/captchaPage.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(captcha);
                stage.setScene(scene);
                stage.showAndWait();

                //Load Email Verification, pass in the email we are using for the user in order to dynamically
                //Send verification emails
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/FXML/EmailValidation.fxml"));
                Parent emailValidationParent = loader.load();
                Scene sceneEmail = new Scene(emailValidationParent);

                //Send an initial token email, after this is done, open the confirmation window
                EmailValidationController controller = loader.getController();
                String token = TokenHandler.createUniqueToken(5);
                eh.sendVerification(session, email, token);
                db.addTokenEntry(token, (int)System.currentTimeMillis()/1000);
                stage.setScene(sceneEmail);
                controller.initData(email);
                stage.showAndWait();

                //Once Captcha has been confirmed, Create a User Object and in turn, a DB User object
                User user = new User(firstName, lastName, User.Sex.valueOf(sex.toUpperCase(Locale.ROOT)), height, weight, dob, email, username);
                db.createUserEntry(user, password);





            }
        }
        else{
            submitButton.setTextFill(Paint.valueOf("#bf2323"));
        }
    }

    public void TermsAndCons() throws IOException {
        Parent part = FXMLLoader.load(getClass().getResource("/FXML/TermsAndCons.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(part);
        stage.setScene(scene);
        stage.show();
    }

    @FXML protected boolean CheckFirstName(){
        String name = firstNameField.getText();
        if (name.matches(FIRSTNAMEREGEX)){
            firstNamePopUp.setText("");
            return true;
        }
        else{
            firstNamePopUp.setText("Please enter a valid Name");
            return false;
        }
    }

    @FXML protected boolean CheckLastName(){
        String name = lastNameField.getText();
        if (name.matches(LASTNAMEREGEX)){
            lastNamePopUp.setText("");
            return true;
        }
        else{
            lastNamePopUp.setText("Please enter a valid Name.");
            return false;
        }
    }

    @FXML protected boolean CheckEmail(){
        //needs to check for email ragax
        String email = emailField.getText();
        if (email.matches(EMAILREGEX)){
            emailPopUp.setText("");
            return true;
        }
        else{
            emailPopUp.setText("Please Enter Valid Email!");
            return false;
        }
    }

    @FXML protected boolean CheckUsername(){
        String username = usernameField.getText();
        if (username.matches(USERNAMEREGEX)){
            usernamePopUp.setText("");
            return true;
        }
        else{
            usernamePopUp.setText("Please only include characters and numbers, min 5 characters, max 16");
            return false;
        }
    }

    @FXML protected boolean CheckPassword(){

        if (repeatPasswordField.getText().length() != 0){CheckRepeatPassword();}

        String password = passwordField.getText();
        if(password.matches(PASSWORDREGEX)){
            passwordPopUp.setText("");
            return true;
        }
        else{
            passwordPopUp.setText("Needs to include minimum of 8 Characters, 1 Digit and 1 Special character (@#$%^&+=)");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sexCombo.getItems().removeAll(sexCombo.getItems());
        sexCombo.getItems().addAll("Male", "Female", "Other");
    }
}
