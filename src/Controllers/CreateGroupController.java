package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.Goal;
import sample.User;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CreateGroupController implements Initializable {

    @FXML private TextField groupNameField;
    @FXML private Button confirmButton;
    @FXML private Label errorLabel;

    private DatabaseHandler dh;
    private User user;

    /**
     * Method to allow data to be passed into this scene from another
     *
     * @param user Takes in a user object
     */
    public void initData(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dh = DatabaseHandler.getInstance();

    }

    public void createGroup(ActionEvent actionEvent) {

        if(!groupNameField.getText().isEmpty()){

            if(dh.createGroup(groupNameField.getText(), user.getUsername())){

                Stage stage = (Stage) groupNameField.getScene().getWindow();

                stage.close();

            } else {

                errorLabel.setText("Group name already taken, please choose another one.");

            }

        } else {
            errorLabel.setText("Please enter a name.");
        }

    }

}
