package Controllers;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.converter.LocalDateTimeStringConverter;
import sample.DatabaseHandler;
import sample.EmailHandler;
import sample.TokenHandler;

import javax.mail.Session;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;
import java.util.Properties;

/**
 * Class To Control The UIGroupItem, this contains
 *
 * @author Owen Tasker
 *
 * @version 1.0
 */
public class UIGroupItemController {
    @FXML TextField inviteInput;
    @FXML Button inviteButton;
    @FXML Label invitePopUp;
    @FXML Label groupNameLabel;

    @FXML public void inviteButtonAction(){

        String email = DatabaseHandler.getInstance().getEmailFromUsername(inviteInput.getText());
        EmailHandler emailHandler = EmailHandler.getInstance();
        Session emailSession = emailHandler.createSession(emailHandler.SetUpEmailHandler());

        String token = TokenHandler.createUniqueToken(7);

        if (!(email == null)){
            invitePopUp.setText("Invite sent to " + inviteInput.getText());
            DatabaseHandler.getInstance().addInvToken(
                                                        token,
                                                        LocalDateTime.now(),
                                                        groupNameLabel.getText(),
                                                        inviteInput.getText()
            );
            EmailHandler.getInstance().sendGroupInvite(
                                                        emailSession,
                                                        email,
                                                        groupNameLabel.getText(),
                                                        token
            );
        }else{
            invitePopUp.setText("No User Found With This Username");
        }
    }

    public static void main(String[] args) {
        LocalDateTime s = LocalDateTime.now();
        System.out.println(s);
        s = s.plusHours(36);
        System.out.println(s);
    }
}
