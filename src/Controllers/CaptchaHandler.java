package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import sample.DatabaseHandler;
import sample.EmailHandler;
import sample.TokenHandler;
import sample.User;

import javax.mail.Session;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Class to control the FXML document for Captcha Validation
 *
 * @author Evan Clayton
 *
 * @version 1.0
 */
public class CaptchaHandler implements Initializable {

    /* This initialises an array list of the sample captcha names.
    If this app were online a robust library could be used for captcha generation but Java
    lacks any good offline captcha generators. */
    String[] tempArray = {"DmpsTL", "7CTBKQ", "WQESWw", "vVtSZb", "GG1Nov", "yVrDPZ", "OKinC4",
            "oBUwvE", "t3wg3Z", "OwVKkz"};
    private ArrayList<String> captchaList = new ArrayList<>();

    //random number generator for selecting a random index
    private Random generator = new Random();

    private User user;
    private String captcha;
    private byte[] hash;
    private byte[] salt;
    @FXML ImageView captchaImageBox;
    @FXML TextField captchaInput;
    @FXML Label incorrectInputLabel;
    @FXML Button captchaSubmit;
    @FXML Button cancelButton;

    /**
     * Method to randomly select a Captcha image and serve it to the user
     *
     * @return returns the String representation of the captcha
     */
    public String serveCaptcha(){

        //Randomly select a captcha to serve
        int captchaIndex = generator.nextInt(captchaList.size());
        String captcha = captchaList.get(captchaIndex);

        //Print the string representation of the captcha, for diagnostic purposes, will be removed in production
        System.out.println(captcha);

        File file = new File("src/Resources/CaptchaImages/" +captcha+".JPG");

        InputStream captchaFileStream = null;
        try {
            captchaFileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image captchaImage = new Image(captchaFileStream);

        //Set the captcha image
        captchaImageBox.setImage(captchaImage);
        return captcha;
    }

    //this is executed after the FXML elements are initialized and thus the image is served here

    /**
     * Method to run after FXML elements are initialized
     *
     * @param url Called via FXML resource
     * @param resourceBundle Called via the FXML resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        captchaList.addAll(Arrays.asList(tempArray));
        captcha = serveCaptcha();
    }

    /**
     * Pseudo constructor, allows for passing information from one FXML scene to another
     *
     * @param user Takes in a user object, this is to be passed from scene to scene in this fashion
     * @param hash Takes in the password hash
     * @param salt Takes in the password salt
     */
    public void initData(User user, byte[] hash, byte[] salt){
        this.user = user;
        this.hash = hash;
        this.salt = salt;
    }

    /**
     * Method to validate whether user input matches the captcha, if it does, send a preliminary validation email,
     * close the current scene and open the email validation scene
     *
     * @param actionEvent Takes in an event that causes this method to fire
     *
     * @throws IOException throws an exception if there is an input/output error, occurs primarily when a scene is
     *                     not recognized
     */
    public void validateCaptcha(ActionEvent actionEvent) throws IOException {
        //Instantiate Email and Database Handlers, will need email as the initial verification token will be sent from
        //this class at time of submission whereon forward it will be handled by the EmailValidationController
        DatabaseHandler db = DatabaseHandler.getInstance();
        EmailHandler eh = EmailHandler.getInstance();
        Session session = eh.createSession();

        //Check that the user input is the same as the expected value, if so, send a validation code then pass
        //to email validation scene
        if (captchaInput.getText().stripTrailing().stripLeading().stripIndent().equals(captcha)) {

            //Create the initial token to be sent
            String token = TokenHandler.createUniqueToken(5);
            eh.sendVerification(session, user.getEmail(), token);
            db.addTokenEntry(token);

            Stage parentScene = (Stage) captchaSubmit.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/EmailValidation.fxml"));

            Parent emailParent = loader.load();
            Scene sceneEmail = new Scene(emailParent);
            EmailValidationController controller = loader.getController();
            stage.setScene(sceneEmail);
            controller.initData(user, token, hash, salt);

            parentScene.close();
            stage.show();
        }
        //display error if invalid
        else {
            incorrectInputLabel.setText("Incorrect answer, try again.");
            captcha = serveCaptcha();
        }
    }

    /**
     * Method to take the user back to the splash page
     *
     * @param actionEvent Takes in an event that causes this method to fire
     *
     * @throws IOException throws an exception if there is an input/output error, occurs primarily when a scene is
     *                     not recognized
     */
    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) cancelButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/SplashPage.fxml"));

        Parent splashParent = loader.load();
        Scene sceneParent = new Scene(splashParent);
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }

}