package Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A controller for the Group page of the app.
 *
 * @author Charlie Jones
 * @author Samuel Scarfe
 *
 * @version 1.2
 *
 * 1.0 - Initial commit, dummy file.
 * 1.1 - Added initialise function, and populate groups section with all groups the user has joined. It also displays
 *       the group members of each group but not yet the goals.
 * 1.2 - Adding group goal viewing, joining, and setting.
 * 1.3 - Refactored into private methods to improve readability.
 */
public class GroupController implements Initializable {

    @FXML private VBox groupsContainer;
    @FXML private TextField joinGroupInput;
    @FXML private Label joinGroupConfirmPopUp;

    @FXML private TabPane tabPane;
    @FXML private ChoiceBox<String> groupsAdministrated;
    @FXML private TextField dietAmount;
    @FXML private ChoiceBox<String> dietDropDown;
    @FXML private DatePicker dietDate;
    @FXML private Label dietLabel;
    @FXML private TextField calorieAmount;
    @FXML private DatePicker calorieDate;
    @FXML private Label calorieLabel;
    @FXML private TextField exerciseAmount;
    @FXML private ChoiceBox<String> exerciseDropDown;
    @FXML private DatePicker exerciseDate;
    @FXML private Label exerciseLabel;
    @FXML private Button createGroupButton;

    private DatabaseHandler dh;
    private User user;
    private HashMap<String, Goal.Unit> nutrientsMap;

    /**
     * Method to be called once all FXML elements have been loaded, combined with initData acts as a pseudo-constructor
     *
     * @param url FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dh = DatabaseHandler.getInstance();
    }

    /**
     * Method to allow data to be passed into this scene from another
     *
     * @param user Takes in a user object
     */
    public void initData(User user) {
        this.user = user;
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

        groupsAdministrated.getItems().clear();
        dietDropDown.getItems().clear();
        exerciseDropDown.getItems().clear();

        initViewGroups();

        nutrientsMap = new HashMap<>();

        nutrientsMap.put("Calories (kcal)", Goal.Unit.CALORIES); nutrientsMap.put("Protein (g)", Goal.Unit.PROTEIN);
        nutrientsMap.put("Carbs (g)", Goal.Unit.CARBS); nutrientsMap.put("Fibre (g)", Goal.Unit.FIBRE);
        nutrientsMap.put("Sodium (mg)", Goal.Unit.SODIUM); nutrientsMap.put("Potassium (mg)", Goal.Unit.POTASSIUM);
        nutrientsMap.put("Calcium (mg)", Goal.Unit.CALCIUM); nutrientsMap.put("Magnesium (mg)", Goal.Unit.MAGNESIUM);
        nutrientsMap.put("Phosphorus (mg)", Goal.Unit.PHOSPHORUS); nutrientsMap.put("Iron (mg)", Goal.Unit.IRON);
        nutrientsMap.put("Copper (mg)", Goal.Unit.COPPER); nutrientsMap.put("Zinc (mg)", Goal.Unit.ZINC);
        nutrientsMap.put("Chloride (mg)", Goal.Unit.CHLORIDE); nutrientsMap.put("Selenium (ug)", Goal.Unit.SELENIUM);
        nutrientsMap.put("Iodine (ug)", Goal.Unit.IODINE); nutrientsMap.put("Vitamin A (ug)", Goal.Unit.VITAMIN_A);
        nutrientsMap.put("Vitamin D (ug)", Goal.Unit.VITAMIN_D); nutrientsMap.put("Thiamin (mg)", Goal.Unit.THIAMIN);
        nutrientsMap.put("Riboflavin (mg)", Goal.Unit.RIBOFLAVIN); nutrientsMap.put("Niacin (mg)", Goal.Unit.NIACIN);
        nutrientsMap.put("Vitamin B6 (mg)", Goal.Unit.VITAMIN_B6); nutrientsMap.put("Vitamin B12 (ug)", Goal.Unit.VITAMIN_B12);
        nutrientsMap.put("Folate (ug)", Goal.Unit.FOLATE); nutrientsMap.put("Vitamin C (mg)", Goal.Unit.VITAMIN_C);

        groupsAdministrated.getItems().addAll(dh.getGroupsAdministrated(user.getUsername()));
        dietDropDown.getItems().addAll(nutrientsMap.keySet());
        exerciseDropDown.getItems().addAll("Walking", "Jogging", "Running", "Football", "Rugby", "Yoga", "Tennis",
                "Swimming", "Cycling", "Karate", "Hiking", "Cleaning", "Boxing", "Billiards", "Judo");

        //https://stackoverflow.com/questions/43092588/how-to-perform-some-action-when-the-tab-is-selected-in-javafx-scene-builder
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if ("View Groups".equals(t1.getText()))
            {
                initUserGroupData();
            }
        });

        dietDate.setEditable(false);
        calorieDate.setEditable(false);
        exerciseDate.setEditable(false);

        //Set dateAmountField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        dietAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d\\.\\d)*")) {
                dietAmount.setText(newValue.replaceAll("[^(\\d\\.\\d)]", ""));
            }
        });

        //Set calorieAmountField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        calorieAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                calorieAmount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //Set exerciseMinutesField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        exerciseAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                exerciseAmount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Method to control group joining action.
     */
    @FXML
    void joinGroupButtonAction() {
        joinGroupConfirmPopUp.setText(""); //Empty pop up label
        String tokenInput = joinGroupInput.getText(); //Retrieve token input

        //Retrieve user token answer
        int userID = dh.getUserIDFromUsername(user.getUsername());
        String groupName = dh.getGroupNameFromInv(tokenInput);
        int groupInvUserID = dh.getUserIDFromInv(tokenInput);
        LocalDateTime beforeNow = dh.getTimeoutFromInv(tokenInput);

        //If user has been invited and invite has not expired
        if (userID == groupInvUserID && LocalDateTime.now().isBefore(beforeNow)) {
            //if the user is not already a member
            if(!dh.isMemberOfGroup(user.getUsername(), groupName)) {
                dh.joinGroup(user.getUsername(), groupName);
                joinGroupConfirmPopUp.setText("Successfully joined " + groupName);
            }
            else { //User is already a member
                joinGroupConfirmPopUp.setText(
                        "You Cannot Join This Group As You Are Already A Member, " +
                                "this token has been removed from the system"
                );
            }
            dh.deleteGroupInv(tokenInput);
            initUserGroupData();
        }
        else { //User has not been invited, or token has expired.
            if (tokenInput.equals("")) {
                joinGroupConfirmPopUp.setText("Please enter an invite code");
            }
            else {
                joinGroupConfirmPopUp.setText(
                        "Something went wrong when joining the group, please make sure the " +
                                "invite was meant for this user and has not expired (36 hours)"
                );
            }

            if (dh.isInvExpired(tokenInput)) {
                dh.deleteGroupInv(tokenInput);
            }

        }
    }

    /**
     * Method to instantiate all of a User's Group Memberships, each as a box in a scroll pane.
     */
    public void initViewGroups() {

        groupsContainer.getChildren().clear();
        for(Group group : dh.getUserGroups(user.getUsername())) {

            VBox groupNode = null;
            try {
                groupNode = FXMLLoader.load(getClass().getResource("/FXML/UIGroupItem.fxml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if(groupNode != null){

                //Extracting children nodes from UIGroupItem.fxml
                ObservableList<Node> children = groupNode.getChildren();

                //Get group name label from node list
                HBox groupNameLabelHbox = (HBox) children.get(0);
                Label groupNameLabel = (Label) groupNameLabelHbox.getChildren().get(0);
                groupNameLabel.setText(group.getName());

                //Get group container from node list
                HBox groupContainer = (HBox) children.get(1);

                //Get user list section
                VBox userListVBox = (VBox) groupContainer.getChildren().get(0);
                ScrollPane userListScroll = (ScrollPane) userListVBox.getChildren().get(1);
                VBox userList = (VBox) userListScroll.getContent();

                //Display members
                setMembers(group, userList);

                //Get the role of the user
                String userRole = DatabaseHandler.getInstance().getGroupRoleFromUsername(groupNameLabel.getText(), user.getUsername());

                //Set permissions based on role
                setPermissions(userRole, children);

                // Get goal list from groupContainer children
                VBox goalList = (VBox) groupContainer.getChildren().get(2);
                ScrollPane goalScrollPane = (ScrollPane) goalList.getChildren().get(1);
                VBox goalScroller = (VBox) goalScrollPane.getContent();

                //View group goals
                showGroupGoals(group, goalScroller);

                groupsContainer.getChildren().add(groupNode);
            }
        }
    }

    /**
     * Private helper method to show group members and their roles.
     * @param group the group whose members are to be shown.
     * @param userList a VBox wherein the members will be displayed.
     */
    private void setMembers(Group group, VBox userList) {
        //Display owner
        Label ownerLabel = new Label("Owner: " + group.getOwner().getUser().getUsername());
        ownerLabel.setUnderline(true);
        userList.getChildren().add(ownerLabel);

        //Display admins
        for(GroupAdmin groupAdmin : group.getAdmins())
            userList.getChildren().add(new Label("Admin: " + groupAdmin.getUser().getUsername()));

        userList.getChildren().add(new Label(""));

        //Display members
        for(GroupMember groupMember : group.getMembers())
            userList.getChildren().add(new Label(groupMember.getUser().getUsername()));
    }

    /**
     * Private helper method to show variable content based on user permissions in a group.
     *
     * @param userRole the user's role in a group.
     * @param children a list of nodes containing all those nodes with variable permissions.
     */
    private void setPermissions(String userRole, ObservableList<Node> children) {
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

        // Get group ownership transfer button from node list
        HBox groupNameLabelHbox = (HBox) children.get(0);
        Label groupNameLabel = (Label) groupNameLabelHbox.getChildren().get(0);
        Button groupOwnershipTransfer = (Button) groupNameLabelHbox.getChildren().get(1);

        //Get Leave/Delete Group button
        HBox deleteLeaveGroupButtonHBox = (HBox) groupNameLabelHbox.getChildren().get(2);
        Button leaveDeleteButton = (Button) deleteLeaveGroupButtonHBox.getChildren().get(0);

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
            groupOwnershipTransfer.setManaged(false);
            leaveDeleteButton.setText("Leave Group");
            leaveDeleteButton.setOnAction(
                    actionEvent -> UIGroupItemController.leaveGroup(user, groupNameLabel.getText()));
            leaveDeleteButton.setOnMouseClicked(ActionEvent -> initUserGroupData());
        }
        if ((!(userRole == null)) && (userRole.equals("Admin"))) {
            groupOwnershipTransfer.setManaged(false);
            leaveDeleteButton.setText("Leave Group");
            leaveDeleteButton.setOnAction(
                    actionEvent -> UIGroupItemController.leaveGroup(user, groupNameLabel.getText()));
            leaveDeleteButton.setOnMouseClicked(ActionEvent -> initUserGroupData());
        }
        if ((!(userRole == null)) && (userRole.equals("Owner"))) {
            leaveDeleteButton.setText("Delete Group");
            leaveDeleteButton.setOnAction(
                    actionEvent -> UIGroupItemController.deleteGroup(groupNameLabel.getText()));
            leaveDeleteButton.setOnMouseClicked(ActionEvent -> initUserGroupData());
        }
    }

    /**
     * Private helper method to display a Groups goals and allow them to be accepted by users.
     *
     * @param group the group whose goals are to be shown.
     * @param goalScroller a scrollable window in which to show the goals.
     */
    private void showGroupGoals(Group group, VBox goalScroller) {

        //Alternating styles sheets with index to change with every added goal.
        int stylesIndex = 0;
        String[] styles = {"groupGoalsHboxOdd", "groupGoalsHboxEven"};

        for (GroupGoal groupGoal : group.getGroupGoals()) {
            //Show goal string in the centre of an HBox.
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
            }
            else {
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

            //Add label and button to HBox
            HBox hbox = new HBox(innerBox, button);
            hbox.getStyleClass().add(styles[stylesIndex]);
            hbox.setAlignment(Pos.CENTER);
            VBox.setMargin(hbox, new Insets(5, 5, 5, 5));

            //Add goal to screen.
            goalScroller.getChildren().add(hbox);

            stylesIndex = ++stylesIndex%2; //Increment then mod 2
        }
    }

    /**
     * Method for parsing information passed to diet fields and creating a user goal.
     */
    public void setDietGoal() {
        //Get field values
        String groupName = groupsAdministrated.getValue();
        String amountText = dietAmount.getText();
        String unitsText = dietDropDown.getValue();
        LocalDate dateText = dietDate.getValue();

        //Check field values
        if (checkDietInputs(groupName, amountText, unitsText, dateText)) {

            float amount = Float.parseFloat(amountText); //Need to check as a string first to check for empty input.

            GroupGoal goal = new GroupGoal(amount, nutrientsMap.get(unitsText), dateText, dh.getGroupIDFromName(groupName));
            if (dh.addGroupGoal(groupName, goal)) {
                dietLabel.setText("Group Goal added : " + goal);
                initUserGroupData();
            }
            else {
                dietLabel.setText("This goal already exists in " + groupName);
            }
        }
    }

    /**
     * Method for parsing information passed to calorie fields and creating a user goal.
     */
    public void setCaloriesGoal() {
        //Get fields
        String groupName = groupsAdministrated.getValue();
        String amountText = calorieAmount.getText();
        LocalDate dateText = calorieDate.getValue();

        //Check fields
        if (checkCalorieInputs(groupName, amountText, dateText)) {

            int amount = Integer.parseInt(amountText); //Need to check as a string first to check for empty input

            //Create goal and present message to user
            GroupGoal goal = new GroupGoal(amount, Goal.Unit.BURNED, dateText, dh.getGroupIDFromName(groupName));
            if (dh.addGroupGoal(groupName, goal)) {
                calorieLabel.setText("Goal added : " + goal);
                initUserGroupData();
            }
            else {
                calorieLabel.setText(("This goal already exists in " + groupName));
            }
        }
    }

    /**
     * Method for parsing information passed to exercise fields and creating a user goal.
     */
    public void setExerciseGoal() {
        //Get fields
        String groupName = groupsAdministrated.getValue();
        String amountText = exerciseAmount.getText();
        String unitsText = exerciseDropDown.getValue();
        LocalDate dateText = exerciseDate.getValue();

        //Check fields
        if (checkExerciseInputs(groupName, amountText, unitsText, dateText)) {

            int amount = Integer.parseInt(amountText); //Need to check as a string first to check for empty input

            //Create goal and present message to user
            GroupGoal goal;
            if (unitsText.equals("Any Exercise")) {
                goal = new GroupGoal(amount, Goal.Unit.EXERCISE, dateText, dh.getGroupIDFromName(groupName));
            }
            else {
                goal = new GroupGoal(amount, Goal.Unit.valueOf(unitsText.toUpperCase(Locale.ROOT)), dateText, dh.getGroupIDFromName(groupName));
            }
            if (dh.addGroupGoal(groupName, goal)) {
                exerciseLabel.setText("Goal added : " + goal);
                initUserGroupData();
            }
            else {
                exerciseLabel.setText("This goal already exists in " + groupName);
            }
        }
    }

    /**
     * Private helper method for checking diet fields and presenting user prompts.
     *
     * @param amount the amount to consume
     * @param units the units of consumption
     * @param date the end date for the goal
     * @return a boolean value representing whether the fields were completed correctly
     */
    private boolean checkDietInputs(String groupName, String amount, String units, LocalDate date) {
        if (groupName == null) {
            dietLabel.setText("Please select an administrated group");
            return false;
        }
        if (amount == null || amount.equals("")) { //Empty input
            dietLabel.setText("Please enter an amount");
            return false;
        }
        else if (Integer.parseInt(amount) == 0) { //Zero input
            dietLabel.setText("Amount cannot be 0");
            return false;
        }
        else if (units == null || units.equals("")) { //Units not selected
            dietLabel.setText("Please select units");
            return false;
        }
        else if (date == null) { //Date not selected
            dietLabel.setText("Please select a date");
            return false;
        }
        else if (!date.isAfter(LocalDate.now())) { //Date in past
            dietLabel.setText("Date must be in the future");
            return false;
        }

        return true; //All fields fine
    }

    /**
     * Private helper method for checking calorie fields and presenting user prompts.
     *
     * @param amount the amount to burn
     * @param date the end date for the goal
     * @return a boolean value representing whether the fields were completed correctly
     */
    private boolean checkCalorieInputs(String groupName, String amount, LocalDate date) {
        if (groupName == null) {
            calorieLabel.setText("Please select an administrated group");
            return false;
        }
        if (amount == null || amount.equals("")) { //Empty input
            calorieLabel.setText("Please enter an amount");
            return false;
        }
        else if (Integer.parseInt(amount) == 0) { //Zero input
            calorieLabel.setText("Amount cannot be 0");
            return false;
        }
        else if (date == null) { //Date not selected
            calorieLabel.setText("Please select a date");
            return false;
        }
        else if (!date.isAfter(LocalDate.now())) { //Date in past
            calorieLabel.setText("Date must be in the future");
            return false;
        }

        return true; //All fields fine
    }

    /**
     * Private helper method for checking exercise fields and presenting user prompts.
     *
     * @param amount the amount of minutes to exercise
     * @param units the type of exercise
     * @param date the end date for the goal
     * @return a boolean value representing whether the fields were completed correctly
     */
    private boolean checkExerciseInputs(String groupName, String amount, String units, LocalDate date) {
        if (groupName == null) {
            exerciseLabel.setText("Please select an administrated group");
            return false;
        }
        if (amount == null || amount.equals("")) { //Empty input
            exerciseLabel.setText("Please enter an amount");
            return false;
        }
        else if (Integer.parseInt(amount) == 0) { //Zero input
            exerciseLabel.setText("Amount cannot be 0");
            return false;
        }
        else if (units == null || units.equals("")) { //Exercise not selected
            exerciseLabel.setText("Please select an exercise");
            return false;
        }
        else if (date == null) { //Date not selected
            exerciseLabel.setText("Please select a date");
            return false;
        }
        else if (!date.isAfter(LocalDate.now())) { //Date in past
            exerciseLabel.setText("Date must be in the future");
            return false;
        }

        return true; //All fields fine
    }

    public void createGroup(ActionEvent actionEvent) throws IOException {
//        Stage parentScene = (Stage) createGroupButton.getScene().getWindow();
//        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/CreateGroupPage.fxml"));

        Parent parent = loader.load();

        Stage stage = new Stage();

        Scene sceneParent = new Scene(parent);

        stage.setScene(sceneParent);

        CreateGroupController controller = loader.getController();

        controller.initData(user);

        stage.setMinWidth(350);
        stage.setMinHeight(300);
        stage.setMaxWidth(550);
        stage.setMaxHeight(500);

        stage.setTitle("ProActive - Create a group");

        stage.showAndWait();

        initUserGroupData();

    }
}
