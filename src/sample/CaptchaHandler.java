package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.awt.image.BufferedImage;
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


    //empty constructor, all the main work is done in initialize
    public CaptchaHandler() {
    }

    private String captcha;
    @FXML ImageView captchaImageBox;
    @FXML TextField captchaInput;
    @FXML Label incorrectInputLabel;

    public String serveCaptcha() {
        int captchaIndex = generator.nextInt(captchaList.size());
        String captcha = captchaList.get(captchaIndex);
        System.out.println(captcha);

        //displays captcha image
        File file = new File("src/sample/CaptchaImages/"+captcha+".JPG");
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
        for(String captchaTemp:tempArray){
            captchaList.add(captchaTemp);
        }
        captcha = serveCaptcha();
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
            captcha = serveCaptcha();
        }
    }

}
