package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.EmailHandler;
import sample.TokenHandler;

import javax.mail.Session;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class To Control The UIGroupItem, this contains
 *
 * @author Owen Tasker
 *
 * @version 1.0
 */
public class UIGroupItemController implements Initializable {
    @FXML TextField inviteInput;
    @FXML Button inviteButton;
    @FXML Label invitePopUp;
    @FXML Label groupNameLabel;

    @FXML HBox groupInfoContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //groupInfoContainer.set
    }

    @FXML public void inviteButtonAction(){

        String email = DatabaseHandler.getInstance().getEmailFromUsername(inviteInput.getText());
        EmailHandler emailHandler = EmailHandler.getInstance();
        Session emailSession = emailHandler.createSession();

        String token = TokenHandler.createUniqueToken(7);

        if (!(email == null)){
            if (DatabaseHandler.getInstance().isMemberOfGroup(inviteInput.getText(), groupNameLabel.getText())){
                invitePopUp.setText("You Cannot Invite Users Which Are Already Members Of This Group");
            }else{
                invitePopUp.setText("Invite sent to " + inviteInput.getText());
                DatabaseHandler.getInstance().addInvToken(token, groupNameLabel.getText(), inviteInput.getText());
                EmailHandler.getInstance().sendGroupInvite(emailSession, email, groupNameLabel.getText(), token);
            }
        }else{
            invitePopUp.setText("No User Found With This Username");
        }
    }

    public void changeOwnershipButtonAction() throws IOException {

        FXMLLoader load = new FXMLLoader();

        load.setLocation(getClass().getResource("/FXML/ChangeOwnership.fxml"));

        Parent changeOwnershipParent = load.load();

        Stage stage = new Stage();

        Scene sceneOwnership = new Scene(changeOwnershipParent);

        stage.setScene(sceneOwnership);

        ChangeOwnershipController controller = load.getController();
        stage.setScene(sceneOwnership);
        controller.initData(DatabaseHandler.getInstance().getGroupObjectFromGroupName(groupNameLabel.getText()));

        stage.show();

    }
}
