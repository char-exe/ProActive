package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class to control the Registration Form FXML file
 *
 * @author Andrew Storey
 * @author Owen Tasker
 * @author Samuel Scarfe
 *
 * @version 1.0
 */
public class RegFormController implements Initializable {


    @FXML public Label firstNamePopUp;
    @FXML public Label lastNamePopUp;
    @FXML public Label emailPopUp;
    @FXML public Label usernamePopUp;
    @FXML public Label sexPopUp;
    @FXML public Label passwordPopUp;
    @FXML public Label repeatPasswordPopUp;
    @FXML public Label dateOfBirthPopUp;
    @FXML public Label heightPopUp;
    @FXML public Label weightPopUp;
    @FXML public Button escapeHome;

    @FXML public TextField firstNameField;
    @FXML public TextField lastNameField;
    @FXML public TextField emailField;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField repeatPasswordField;
    @FXML public Hyperlink termsConsLink;
    @FXML public CheckBox termsCheckBox;
    @FXML public Button submitButton;
    @FXML public ChoiceBox<String> sexCombo;
    @FXML public DatePicker dateField;
    @FXML public TextField heightField;
    @FXML public TextField weightField;

    /**
     * Method to be called once all FXML elements have been loaded
     *
     * @param url FXML defined resource
     * @param resourceBundle FXML defined resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sexCombo.getItems().removeAll(sexCombo.getItems());
        sexCombo.getItems().addAll("Male", "Female", "Other");
        dateField.setEditable(false);

        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                heightField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        weightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                weightField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Method to take the inputs from registration form fields, validate them, hash the password and pass a
     * constructed User object into the captcha handler
     *
     * @throws IOException Throws an exception whenever it is possible for a file to be missing
     * @throws NoSuchAlgorithmException Thrown when the specified cryptographic algorithm is not available
     * @throws InvalidKeySpecException Thrown if the key provided does not meet the specification for the cryptographic
     *                                 algorithm
     */
    @FXML protected void handleSubmitButtonAction() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        firstNamePopUp.setText("");
        lastNamePopUp.setText("");
        emailPopUp.setText("");
        usernamePopUp.setText("");
        sexPopUp.setText("");
        passwordPopUp.setText("");
        repeatPasswordPopUp.setText("");
        dateOfBirthPopUp.setText("");
        heightPopUp.setText("");
        weightPopUp.setText("");

        if (checkConditions()){

            //Instantiate Database Handler
            DatabaseHandler db = DatabaseHandler.getInstance();

            //Read in all of the information from the registration form
            String firstName    = firstNameField.getText();
            String lastName     = lastNameField.getText();
            String email        = emailField.getText();
            LocalDate dob       = dateField.getValue();
            String heightText   = heightField.getText();
            String weightText   = weightField.getText();
            String username     = usernameField.getText();
            String sex          = sexCombo.getValue();
            String password     = passwordField.getText();

            //Password hashing
            SecureRandom sr = new SecureRandom();
            byte[] salt = new byte[16];
            sr.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            //Check that username and email inputted was unique
            if (!db.checkUserNameUnique(username)) {
                usernamePopUp.setText("Username Not Unique, Please Enter A New One");
            }
            else if (db.checkEmailUnique(email)) {
                emailPopUp.setText("Email Not Unique, Please Enter A New One");
            }
            else if (dateField.getValue() == null || dateField.getValue().isAfter(LocalDate.now())) {
                dateOfBirthPopUp.setText("Please select your Date of Birth");
            }
            else if (heightText == null || heightText.equals("")) {
                heightPopUp.setText("Please input your height");
            }
            else if (Integer.parseInt(heightText) < 1 || 349 < Integer.parseInt(heightText)) {
                heightPopUp.setText("Height must be between 1 and 349");
            }
            else if (weightText == null || weightText.equals("")) {
                weightPopUp.setText("Please input your weight");
            }
            else if (Integer.parseInt(weightText) < 1 || 1499 < Integer.parseInt(weightText)) {
                weightPopUp.setText("Weight must be between 1 and 1499");
            }
            else if (sex == null) {
                sexPopUp.setText("Please select a sex");
            }
            else {
                float height = Float.parseFloat(heightText);
                float weight = Float.parseFloat(weightText);

                Stage parentScene = (Stage) submitButton.getScene().getWindow();
                Stage stage = new Stage();
                User user = new User(firstName, lastName, User.Sex.valueOf(sex.toUpperCase(Locale.ROOT)), height, weight, dob, email, username);

                FXMLLoader load = new FXMLLoader();

                load.setLocation(getClass().getResource("/FXML/CaptchaPage.fxml"));

                Parent captchaParent = load.load();

                Scene sceneCaptcha = new Scene(captchaParent);

                stage.setScene(sceneCaptcha);

                CaptchaHandler controller = load.getController();
                stage.setScene(sceneCaptcha);
                controller.initData(user, hash, salt);

                parentScene.close();
                stage.show();
            }
        }
        else {
            submitButton.setTextFill(Paint.valueOf("#bf2323"));
        }
    }

    /**
     * Method to compress registration form checks
     *
     * @return Returns true if all checks are true, false otherwise
     */
    public boolean checkConditions() {
        return CheckFirstName() && CheckLastName() && CheckEmail() && CheckPassword() && CheckRepeatPassword() && CheckTermsBox() && CheckUsername();
    }

    /**
     * Method to call a terms and conditions page in another scene if requested
     *
     * @throws IOException Throws an IOException if there is a chance a file does not exist
     */
    public void TermsAndCons() throws IOException {
        Parent part = FXMLLoader.load(getClass().getResource("/FXML/TermsAndCons.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(part);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to check firstname field matches in regex
     *
     * @return returns true if check is passed
     */
    @FXML protected boolean CheckFirstName() {
        String name = firstNameField.getText();
        //Regexes for checking components
        String FIRSTNAMEREGEX = "[A-Z][a-z]*";
        if (name.matches(FIRSTNAMEREGEX)){
            firstNamePopUp.setText("");
            return true;
        }
        else {
            firstNamePopUp.setText("Please enter a valid Name");
            return false;
        }
    }

    /**
     * Method to check lastname field matches in regex
     *
     * @return returns true if check is passed
     */
    @FXML protected boolean CheckLastName() {
        String name = lastNameField.getText();
        String LASTNAMEREGEX = "[a-zA-Z]+([ '-][a-zA-Z]+)*";
        if (name.matches(LASTNAMEREGEX)) {
            lastNamePopUp.setText("");
            return true;
        }
        else {
            lastNamePopUp.setText("Please enter a valid Name.");
            return false;
        }
    }

    /**
     * Method to check email field matches in regex
     *
     * @return returns true if check is passed
     */
    @FXML protected boolean CheckEmail() {
        //needs to check for email regex
        String email = emailField.getText();
        String EMAILREGEX = "^\\w+.?\\w+@\\w+[.]\\w+([.]\\w+){0,2}$";
        if (email.matches(EMAILREGEX)) {
            emailPopUp.setText("");
            return true;
        }
        else {
            emailPopUp.setText("Please Enter Valid Email");
            return false;
        }
    }

    /**
     * Method to check username field matches in regex
     *
     * @return returns true if check is passed
     */
    @FXML protected boolean CheckUsername() {
        String username = usernameField.getText();
        String USERNAMEREGEX = "^[a-zA-Z0-9_-]{5,16}$";
        if (username.matches(USERNAMEREGEX)) {
            usernamePopUp.setText("");
            return true;
        }
        else {
            usernamePopUp.setText("Please only include characters and numbers, min 5 characters, max 16");
            return false;
        }
    }

    /**
     * Method to check password field matches in regex
     *
     * @return returns true if check is passed
     */
    @FXML protected boolean CheckPassword() {

        if (repeatPasswordField.getText().length() != 0) {
            CheckRepeatPassword();
        }

        String password = passwordField.getText();
        String PASSWORDREGEX = "(?=.*\\w)(?=.*[!@#$%^&+='()*,./:;<>?{|}~])(?=\\S+$).{8,}";
        if(password.matches(PASSWORDREGEX)) {
            passwordPopUp.setText("");
            return true;
        }
        else {
            passwordPopUp.setText("Needs to include minimum of 8 Characters, 1 Digit and 1 Special character (@#$%^&+=)");
            return false;
        }
    }

    /**
     * Method to check both password and repeat password match
     *
     * @return returns true if check is passed
     */
    @FXML protected boolean CheckRepeatPassword() {
        if (!passwordField.getText().equals(repeatPasswordField.getText())) {
            repeatPasswordPopUp.setText("Passwords do not match.");
            submitButton.setTextFill(Paint.valueOf("darkred"));
            return false;
        }
        else {
            submitButton.setTextFill(Paint.valueOf("black"));
            repeatPasswordPopUp.setText("");
            return true;
        }
    }

    /**
     * Method to check that user has ticked the T&C box
     *
     * @return returns true if check is passed
     */
    @FXML protected boolean CheckTermsBox() {
        if (termsCheckBox.isSelected()&&!termsConsLink.getTextFill().equals(Paint.valueOf("darkred"))) {
            return true;
        }
        else if(termsCheckBox.isSelected()&&termsConsLink.getTextFill().equals(Paint.valueOf("darkred"))) {
            termsConsLink.setTextFill(Paint.valueOf("black"));
            submitButton.setTextFill(Paint.valueOf("black"));
            return true;
        }
        else {
            termsConsLink.setTextFill(Paint.valueOf("darkred"));
            submitButton.setTextFill(Paint.valueOf("darkred"));
            return false;
        }
    }

    /**
     * Method to cancel registration and send user back to the splash page
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void escapeHomeAction() throws IOException {
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

