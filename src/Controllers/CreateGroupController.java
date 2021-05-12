package Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class to control the Create Group page of the ProActive app.
 *
 * @author Charlie Jones
 *
 * @version 1.0
 *
 * 1.0 - First working version.
 */
public class CreateGroupController implements Initializable {

    @FXML private TextField groupNameField;
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

    /**
     * Method to run after all FXML elements have been loaded, used for imposing restrictions on FXML elements
     *
     * @param url FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dh = DatabaseHandler.getInstance();
    }

    /**
     * Method to control the action of the create group button.
     */
    public void createGroup() {

        if(!groupNameField.getText().isEmpty()){

            if(dh.createGroup(groupNameField.getText(), user.getUsername())){

                Stage stage = (Stage) groupNameField.getScene().getWindow();

                stage.close();
            }
            else {
                errorLabel.setText("Group name already taken, please choose another one.");
            }
        }
        else {
            errorLabel.setText("Please enter a name.");
        }
    }
}
