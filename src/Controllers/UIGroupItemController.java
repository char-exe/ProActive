package Controllers;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.DatabaseHandler;
import sample.EmailHandler;
import sample.TokenHandler;

import javax.mail.Session;
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
        DatabaseHandler.getInstance().addTokenEntry(token, System.currentTimeMillis()/1000);

        if (!(email == null)){
            invitePopUp.setText("Invite sent to " + inviteInput.getText());
            EmailHandler.getInstance().sendGroupInvite(emailSession, email, groupNameLabel.getText(), token);
        }else{
            invitePopUp.setText("No User Found With This Username");
        }
    }
}
