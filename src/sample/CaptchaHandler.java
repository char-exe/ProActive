package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

public class CaptchaHandler implements Initializable {

    /* This initialises an array list of the sample captcha names.
    If this app were online a robust library could be used for captcha generation but Java
    lacks any good offline captcha generators. */
    String[] tempArray = {"DmpsTL", "7CTBKQ", "WQESWw", "vVtSZb", "GG1Nov", "yVrDPZ", "OKinC4",
            "oBUwvE", "t3wg3Z", "OwVKkz"};
    private ArrayList<String> captchaList = new ArrayList<String>();

    //random number generator for selecting a random index
    private Random generator = new Random();
    private String captcha;

    //constructor, this gets called before any FXML so it only populates the ArrayList
    public CaptchaHandler() {
        for(String captcha:tempArray){
            captchaList.add(captcha);
        }
        //int captchaIndex = generator.nextInt(captchaList.size());
        int captchaIndex = 1;
        String captcha = captchaList.get(captchaIndex);
        System.out.println(captcha);
    }


    @FXML ImageView captchaImageBox;
    @FXML TextField captchaInput;
    @FXML Label incorrectInputLabel;

    //this is executed after the FXML elements are initialized and thus the image is served here
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("src/CaptchaImages/"+captcha+".jpg");
        Image captchaImage = new Image(file.toURI().toString());
        captchaImageBox.setImage(captchaImage);
    }

    //checks to see if user's input matches the captcha content
    public void validateCaptcha(ActionEvent actionEvent) {
        System.out.println(captchaInput.getText());
        String input = captchaInput.getText().stripTrailing().stripLeading().stripIndent();

        //redirect if valid
        if (input.equals(captcha)) {
            System.out.println("Valid");
        }
        //display error if invalid
        else {
            incorrectInputLabel.setText("Incorrect answer, try again.");
        }
    }

}
