package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.EmailHandler;
import sample.TokenHandler;
import sample.User;

import javax.mail.Session;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmailValidationController implements Initializable{
    @FXML public Label header;
    @FXML public Button submitButton;
    @FXML public Label sendStatus;
    @FXML public TextField codeInputBox;
    @FXML public Label codeStatus;
    @FXML public Button resendButton;

    private String email;

    public void initData(String email){
        this.email = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML protected void pushSubmit() throws SQLException {
        String tokenFromUser = codeInputBox.getText();
        DatabaseHandler dh = new DatabaseHandler("jdbc:sqlite:proactive.db");
        int currentTime = (int) (System.currentTimeMillis()/1000);
        ResultSet res = dh.getTokenResult(tokenFromUser);
        if (res == null){
            codeStatus.setText("Code Incorrect, Please Try Again");
        }else{
            long serverTime = Long.parseLong(res.getString("timeDelay"));
            if (serverTime > currentTime + 1800){
                System.out.println("Server Time: " + serverTime);
                System.out.println("Current Time: " + currentTime);
                codeStatus.setText("Code Correct, User Account has been created");
                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            }else{
                codeStatus.setText("Code Has Expired, Another Has Been Sent, Please Try Again");
                resendVerification();
            }
        }
    }


    @FXML protected void resendVerification(){

        sendStatus.setText("We Have Resent A Code");

        DatabaseHandler dh = new DatabaseHandler("jdbc:sqlite:proactive.db");
        long time = System.currentTimeMillis()/1000;
        String token = TokenHandler.createUniqueToken(7);

        EmailHandler eh = new EmailHandler("proactivese13@gmail.com", "f45d09mFAcHr");
        Properties prop = eh.SetUpEmailHandler();
        Session session = eh.createSession(prop);

        String emailToSendTo = email;
        System.out.println("Email Sending To: " + emailToSendTo);
        dh.addTokenEntry(token, (int) time);
        eh.sendVerification(session, emailToSendTo, token);
    }

    public static void main(String[] args) {
        int t = (int) (System.currentTimeMillis()/1000);
        long t2 = System.currentTimeMillis()/1000;
        System.out.println(t);
        System.out.println(t2);
    }
}
