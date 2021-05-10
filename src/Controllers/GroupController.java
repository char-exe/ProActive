package Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import sample.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
    @FXML private TextField joinGroupInput;
    @FXML private Label joinGroupConfirmPopUp;


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


    @FXML void joinGroupButtonAction(ActionEvent actionEvent) throws SQLException {
        joinGroupConfirmPopUp.setText("");
        String tokenInput = joinGroupInput.getText();
        int userID = dh.getUserIDFromUsername(user.getUsername());
        String groupName = dh.getGroupNameFromInv(tokenInput);
        int groupInvUserID = dh.getUserIDFromInv(tokenInput);
        LocalDateTime beforeNow = dh.getTimeoutFromInv(tokenInput);

        System.out.println("Got here");
        if (userID == groupInvUserID && LocalDateTime.now().isBefore(beforeNow)){
            if(!dh.isMemberOfGroup(user.getUsername(), groupName)) {
                dh.joinGroup(user.getUsername(), groupName);
                joinGroupConfirmPopUp.setText("Successfully joined " + groupName);
            }else {
                joinGroupConfirmPopUp.setText("You Cannot Join This Group As You Are Already A Member, " +
                                              "this token has been removed from the system"
                                             );

            }
            dh.deleteGroupInv(tokenInput);
        }else{
            joinGroupConfirmPopUp.setText("Something went wrong when joining the group, please make sure the " +
                                          "invite was meant for this user and has not expired (36 hours)"
                                          );

            if (dh.isInvExpired(tokenInput)){
                dh.deleteGroupInv(tokenInput);
            }

        }
    }

    public void initUserGroupData(){

        for(Group group : dh.getUserGroups(user.getUsername())) {
            VBox groupNode = null;
            try {
                groupNode = FXMLLoader.load(getClass().getResource("/FXML/UIGroupItem.fxml"));
            }
            catch (IOException e) {
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
                ScrollPane goalScrollPane = (ScrollPane) goalList.getChildren().get(1);
                VBox goalScroller = (VBox) goalScrollPane.getContent();

                //Seperator between group details and group invite settings
                Separator s = (Separator) children.get(2);

                //Determine logic on allowing members to send invites
                VBox groupInvite = (VBox) children.get(3);

                //Get the Label to be modified for group invites
                HBox groupInviteLabelHbox = (HBox) groupInvite.getChildren().get(0);
                Label groupInvitePopUp = (Label) groupInviteLabelHbox.getChildren().get(0);
                //Get the TextField to be modified for group invites

                HBox groupInviteInputHbox = (HBox) groupInvite.getChildren().get(1);
                Label groupInviteInputLabel = (Label) groupInviteInputHbox.getChildren().get(0);
                TextField groupInviteInput = (TextField) groupInviteInputHbox.getChildren().get(1);

                //Get the Button to be modified for group invites
                HBox groupInviteButtonHbox = (HBox) children.get(4);
                Button groupInviteButton = (Button) groupInviteButtonHbox.getChildren().get(0);

                //Get the role of the user
                String userRole = DatabaseHandler.getInstance().getGroupRoleFromUsername(groupNameLabel.getText(), user.getUsername());

                //If a user is an admin or an owner of a group, there are no restrictions on whether they can invite
                //users to a group
                if ((!(userRole == null)) && userRole.equals("Member")) {
                    s.setManaged(false);
                    groupInvite.setManaged(false);
                    groupInviteLabelHbox.setManaged(false);
                    groupInvitePopUp.setManaged(false);
                    groupInviteInputHbox.setManaged(false);
                    groupInviteInput.setManaged(false);
                    groupInviteButtonHbox.setManaged(false);
                    groupInviteButton.setManaged(false);
                    groupInviteInputLabel.setManaged(false);
                }

                int stylesIndex = 0;
                String[] styles = {"groupGoalsHboxOdd", "groupGoalsHboxEven"};

                for (GroupGoal groupGoal : group.getGroupGoals()) {
                    System.out.println(groupGoal);
                    Region region1 = new Region();
                    Region region2 = new Region();
                    HBox.setHgrow(region1, Priority.ALWAYS);
                    HBox.setHgrow(region2, Priority.ALWAYS);
                    Label label = new Label(groupGoal.toString());
                    HBox innerBox = new HBox(region1, label, region2);
                    HBox.setHgrow(innerBox, Priority.ALWAYS);
                    innerBox.setAlignment(Pos.CENTER);


                    //Create button
                    Button button = new Button();

                    //Set button text based on goal status
                    if (user.getGoals().contains(groupGoal)) {
                        button.setText("Accepted");
                    } else {
                        button.setText("Click to accept");
                    }

                    button.setMinWidth(95); //Width set such that it doesn't change when text changes

                    //Set button action
                    button.setOnAction(e -> {
                        if (!user.getGoals().contains(groupGoal)) { //If goal not labelled accepted
                            user.addGoal(new GroupGoal(
                                    groupGoal.getTarget(),
                                    groupGoal.getUnit(),
                                    groupGoal.getEndDate(),
                                    groupGoal.getGroupId())
                            ); //Add to the user's individual goals
                            button.setText("Accepted"); //Update button text
                        }
                    });

                    HBox hbox = new HBox(innerBox, button);
                    hbox.getStyleClass().add(styles[stylesIndex]);
                    hbox.setAlignment(Pos.CENTER);
                    VBox.setMargin(hbox, new Insets(5, 5, 5, 5));

                    goalScroller.getChildren().add(hbox);


                    stylesIndex = ++stylesIndex%2; //Increment then mod 2
                }

                groupsContainer.getChildren().add(groupNode);

            }
        }

    }

}
