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
import java.nio.file.Path;
import java.util.*;

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
    private byte[] hash;
    private byte[] salt;
    private String captcha;
    @FXML ImageView captchaImageBox;
    @FXML TextField captchaInput;
    @FXML Label incorrectInputLabel;
    @FXML Button captchaSubmit;
    @FXML Button homeButton;

    public String serveCaptcha() {
        int captchaIndex = generator.nextInt(captchaList.size());
        String captcha = captchaList.get(captchaIndex);
        System.out.println(captcha);

        //displays captcha image
        File file = new File("src/Resources/CaptchaImages/" +captcha+".JPG");

        InputStream captchaFileStream = null;
        try {
            captchaFileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image captchaImage = new Image(captchaFileStream);
        captchaImageBox.setImage(captchaImage);
        return captcha;
    }

    //this is executed after the FXML elements are initialized and thus the image is served here
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        captchaList.addAll(Arrays.asList(tempArray));
        captcha = serveCaptcha();
    }

    public void initData(User user, byte[] hash, byte[] salt){
        this.user = user;
        this.hash = hash;
        this.salt = salt;
    }

    //checks to see if user's input matches the captcha content and if so, pass to email validation
    public void validateCaptcha(ActionEvent actionEvent) throws IOException {
        //Instantiate Email and Database Handlers, will need email as the initial verification token will be sent from
        //this class at time of submission whereon forward it will be handled by the EmailValidationController
        DatabaseHandler db = new DatabaseHandler("jdbc:sqlite:proactive.db");
        EmailHandler eh = new EmailHandler("proactivese13@gmail.com", "f45d09mFAcHr");
        Properties prop = eh.SetUpEmailHandler();
        Session session = eh.createSession(prop);

        //Check that the user input is the same as the expected value, if so, send a validation code then pass
        //to email validation scene
        if (captchaInput.getText().stripTrailing().stripLeading().stripIndent().equals(captcha)) {

            //Create the initial token to be sent
            String token = TokenHandler.createUniqueToken(5);
            eh.sendVerification(session, user.getEmail(), token);
            db.addTokenEntry(token, System.currentTimeMillis()/1000);

            Stage parentScene = (Stage) captchaSubmit.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/EmailValidation.fxml"));

            Parent emailParent = loader.load();

            Scene sceneEmail = new Scene(emailParent);

            stage.setScene(sceneEmail);

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

    public void escapeHome(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) homeButton.getScene().getWindow();
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
