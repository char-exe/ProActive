package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import sample.DatabaseHandler;
import sample.User;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class to control the Manage Profile Page FXML file
 *
 * @author Owen Tasker
 *
 * @version 1.0
 */
public class ManageProfilePageController implements Initializable {

    private User user;

    @FXML public ChoiceBox sexCombo;
    @FXML public DatePicker datePick;
    @FXML public TextArea heightField;
    @FXML public Button submitButton;

    /**
     * Pseudo-constructor for controllers, runs after FXML elements have been loaded in and as such allows for
     * modifying their behaviours (e.g. choicebox contents)
     *
     * @param url            FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sexCombo.getItems().removeAll(sexCombo.getItems());
        sexCombo.getItems().addAll("Male", "Female", "Other");
    }

    /**
     * Method to allow for data to be transferred from another scene to this one
     *
     * @param user Takes in a User object to help with persistence
     */
    public void initData(User user) {
        this.user = user;
    }

    /**
     * Method that handles the submit button action, checks to see which values have been modified, then performs
     * update operations directly on the database user object
     *
     * @param event Takes in the event that causes this method to be called
     * @throws IOException Throws an IOException whenever there is a chance for a file to be missing
     */
    //TODO link to the submit button in the FXML document
    @FXML public void submitButtonAction(ActionEvent event) throws IOException {
        System.out.println("submitButton");
        DatabaseHandler dbh = new DatabaseHandler("jdbc:sqlite:proactive.db");

        //Take in the value provided by the datepicker
        LocalDate date = datePick.getValue();
        if (date != null) {
            //TODO add some error checking here to make sure that the date is not past the current day and if it is,
            // before making a database request, show an error to the user and prompt a change
            dbh.editValue("user", "dob", date.toString(), user.getUsername());
        }

        //Take in the value provided to height value
        //TODO Ensure that only numbers can be inputted, TextFormatter seems to be the solution to this
        String updatedHeight = heightField.getText();
        if (updatedHeight != null) {
            dbh.editValue("user", "height", Integer.parseInt(updatedHeight), user.getUsername());
        }

        //Takes in the value provided by sexCombo
        Enum<User.Sex> userSex;
        try {
            userSex = User.Sex.valueOf(sexCombo.getValue().toString().toUpperCase(Locale.ROOT));
        } catch (EnumConstantNotPresentException e1) {
            userSex = null;
        }
        if (userSex != null) {
            dbh.editValue("user", "sex", userSex.toString(), user.getUsername());
        }


    }
}

