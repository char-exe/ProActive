package Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.Group;
import sample.GroupAdmin;

import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Change Group Ownership page of the ProActive app.
 *
 * @author Owen Tasker
 *
 * @version 1.0
 *
 * 1.0 - First working version.
 */
public class ChangeOwnershipController implements Initializable {
    private Group group;
    private int confirmCount = 0;

    @FXML ComboBox<GroupAdmin> adminComboBox;
    @FXML Label adminComboBoxPopUp;
    @FXML Button cancelButton;
    @FXML Label confirmPopUp;


    /**
     * Method for stuff to run after FXML elements are initialized, allows for events to be called as soon as this
     * controller takes over.
     *
     * @param url Called via FXML resource
     * @param resourceBundle Called via FXML resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Pseudo constructor for FXML, allows for passing information between scenes
     *
     * @param group the Group for whom to change ownership.
     */
    public void initData(Group group){
        this.group = group;

        adminComboBox.getItems().addAll(group.getAdmins());
    }

    /**
     * Method for the action of the cancel button.
     */
    public void cancelButtonAction() {
        Stage parentScene = (Stage) cancelButton.getScene().getWindow();

        parentScene.close();
    }

    /**
     * Method for the action of the confirm button.
     */
    public void confirmButtonAction() {
        if (confirmCount == 1) {
            DatabaseHandler.getInstance().changeGroupOwnership(group, adminComboBox.getValue().getUser());
            confirmPopUp.setTextFill(Paint.valueOf("#00FF00"));
            confirmPopUp.setText("Group Ownership Completed, " + adminComboBox.getValue().getUser().getUsername() +
                                 " Is now the new owner of the group");
        }
        if (confirmCount == 0) {
            confirmCount++;
            confirmPopUp.setText("Are you sure you want to transfer ownership, you cannot reverse this change. " +
                                 "Press confirm again to complete this action");
        }
    }
}
