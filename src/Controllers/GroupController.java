package Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import sample.DatabaseHandler;
import sample.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class GroupController implements Initializable {

    @FXML private VBox groupsContainer;


    private DatabaseHandler dh;
    private User user;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

            // Get group container from node list
            HBox groupContainer = (HBox) children.get(1);

            // Get user list from groupContainer children
            VBox userList = (VBox) groupContainer.getChildren().get(0);

            for (int i = 0; i < 3; i++) {
                userList.getChildren().add(new Label("User: "+i));
            }

            // Get goal list from groupContainer children
            VBox goalList = (VBox) groupContainer.getChildren().get(2);

            groupsContainer.getChildren().add(groupNode);

        }

    }

    /**
     * Method to allow data to be passed into this scene from another
     *
     * @param user Takes in a user object
     */
    public void initData(User user) {
        this.user = user;
    }

}
