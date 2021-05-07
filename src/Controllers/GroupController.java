package Controllers;

import com.sun.javafx.scene.control.InputField;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import sample.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the Group page of the app.
 *
 * @author Charlie Jones
 *
 * @version 1.1
 *
 * 1.0 - Initial commit, dummy file.
 * 1.1 - Added initialise function, and populate groups section with all groups the user has joined. It also displays
 *  the group members of each group but not yet the goals
 */
public class GroupController implements Initializable {

    @FXML private VBox groupsContainer;
    @FXML private HBox hboxGroupInvite;
    @FXML private VBox vboxGroupInvite;

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

    public void initUserGroupData(){

        for(Group group : dh.getUserGroups(user.getUsername())){
            VBox groupNode = null;
            try {
                groupNode = FXMLLoader.load(getClass().getResource("/FXML/UIGroupItem.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(groupNode != null){

                // Extracting children nodes from UIGroupItem.fxml
                ObservableList<Node> children = groupNode.getChildren();

                // Get group name label from node list
                Label groupNameLabel = (Label) children.get(0);
                groupNameLabel.setText(group.getName());

                // Get group container from node list
                HBox groupContainer = (HBox) children.get(1);

                // Get user list from groupContainer children
                VBox userList = (VBox) groupContainer.getChildren().get(0);

                Label ownerLabel = new Label("Owner: " + group.getOwner().getUser().getUsername());
                ownerLabel.setUnderline(true);
                userList.getChildren().add(ownerLabel);

                for(GroupAdmin groupAdmin : group.getAdmins())
                    userList.getChildren().add(new Label("Admin: " + groupAdmin.getUser().getUsername()));

                userList.getChildren().add(new Label(""));

                for(GroupMember groupMember : group.getMembers())
                    userList.getChildren().add(new Label(groupMember.getUser().getUsername()));


                // Get goal list from groupContainer children
                VBox goalList = (VBox) groupContainer.getChildren().get(2);

                //Determine logic on allowing members to send invites
                VBox groupInvite = (VBox) children.get(3);

                //Get the Label to be modified for group invites
                HBox groupInviteLabelHbox = (HBox) groupInvite.getChildren().get(0);
                Label groupInvitePopUp = (Label) groupInviteLabelHbox.getChildren().get(0);

                //Get the TextField to be modified for group invites
                HBox groupInviteInputHbox = (HBox) groupInvite.getChildren().get(1);
                TextField groupInviteInput = (TextField) groupInviteInputHbox.getChildren().get(1);

                //Get the Button to be modified for group invites
                HBox groupInviteButtonHbox = (HBox) children.get(4);
                Button groupInviteButton = (Button) groupInviteButtonHbox.getChildren().get(0);

                //Get the role of the user
                String userRole = DatabaseHandler.getInstance().getGroupRoleFromUsername(groupNameLabel.getText(), user.getUsername());

                //If a user is an admin or an owner of a group, there are no restrictions on whether they can invite
                //users to a group
                if(!(userRole == null)){
                    if (!userRole.equals("Member")) {
                        groupInvitePopUp.setText("");
                        groupInviteInput.setEditable(true);
                        groupInviteButton.setDisable(false);
                    }
                    else{
                        groupInvite.setManaged(false);
                        groupInviteInputHbox.setManaged(false);
                    }
                }



                groupsContainer.getChildren().add(groupNode);

            }
        }

    }

}
