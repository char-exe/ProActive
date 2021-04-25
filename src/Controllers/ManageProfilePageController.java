package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.DatabaseHandler;
import sample.User;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class to control the Manage Profile Page FXML file
 *
 * @author Owen Tasker
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - Initial commit
 * 1.1 - Connected to database, added simple error checking.
 */
public class ManageProfilePageController implements Initializable {

    private User user;
    private DatabaseHandler dh;

    @FXML public ChoiceBox<String> sexCombo;
    @FXML public DatePicker datePick;
    @FXML public TextField heightField;
    @FXML public ChoiceBox<String> heightUnits;
    @FXML public Label dobLabel;
    @FXML public Label heightLabel;
    @FXML public Label sexLabel;

    /**
     * Pseudo-constructor for controllers, runs after FXML elements have been loaded in and as such allows for
     * modifying their behaviours (e.g. choicebox contents)
     *
     * @param url            FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dh = DatabaseHandler.getInstance();

        sexCombo.getItems().removeAll(sexCombo.getItems());
        sexCombo.getItems().addAll("Male", "Female", "Other");
        heightUnits.getItems().removeAll(heightUnits.getItems());
        heightUnits.getItems().addAll("cm", "inches");

        //Set heightField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        heightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    heightField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * Method to allow for data to be transferred from another scene to this one
     *
     * @param user Takes in a User object to help with persistence
     */
    public void initData(User user) {
        this.user = user;
        System.out.println(user);
    }

    /**
     * Method that handles the submit button action, checks to see which values have been modified, then performs
     * update operations directly on the database user object
     *
     * @param actionEvent Takes in the event that causes this method to be called
     */
    public void submitButtonAction(ActionEvent actionEvent) {
        System.out.println("submitButton");
        sexLabel.setText("");
        dobLabel.setText("");
        heightLabel.setText("");

        //Take in the value provided by the datepicker
        LocalDate date = datePick.getValue();

        //Try to update database then user. Show appropriate success/fail message to user.
        if (checkDob(date)){
            try {
                dh.editValue("user", "dob", date.toString(), user.getUsername());
                user.setDob(date);
                dobLabel.setText("Date of Birth updated to " + date.toString());
            }
            catch (SQLException e) {
                e.printStackTrace();
                dobLabel.setText("Date of Birth update failed");
            }
        }

        //Take in the value provided to height value
        String updatedHeight = heightField.getText();
        String heightUnit = heightUnits.getValue();

        //Check inputs
        if (checkHeightFields(updatedHeight, heightUnit)) {
            float heightValue = Float.parseFloat(updatedHeight);

            //Convert units and prepare string for printing
            if (heightUnit.equals("inches")) {
                heightValue = (float) (heightValue * 2.5);
                heightUnit = " " + heightUnit;
            }

            //Try to update database then user. Show appropriate success/fail message to user.
            try {
                dh.editValue("user", "height", Integer.parseInt(updatedHeight), user.getUsername());
                user.setHeight(heightValue);
                heightLabel.setText("Height updated to " + updatedHeight + heightUnit);
            }
            catch (SQLException e) {
                e.printStackTrace();
                heightLabel.setText("Height update failed");
            }
        }

        //Takes in the value provided by sexCombo
        String sex = sexCombo.getValue();

        if (sex != null) {
            User.Sex userSex = User.Sex.valueOf(sex.toUpperCase(Locale.ROOT));

            //Try to update database then user. Show appropriate success/fail message to user.
            try {
                dh.editValue("user", "sex", userSex.toString(), user.getUsername());
                user.setSex(userSex);
                sexLabel.setText("Sex updated to " + userSex.toString());
            }
            catch (SQLException e) {
                e.printStackTrace();
                sexLabel.setText("Sex update failed");
            }
        }
    }

    /**
     * Private helper method to check values in the dob field.
     *
     * @param dob the user's new date of birth.
     * @return a boolean value representing whether the inputted values are suitable
     */
    private boolean checkDob(LocalDate dob) {
        if (dob == null) { //Dob not entered, no action needed
            return false;
        }
        else if (dob.isAfter(LocalDate.now())) { //Future date
            dobLabel.setText("Date of Birth cannot be in the future");
            return false;
        }

        return true;
    }

    /**
     * Private helper method for checking the height input fields.
     *
     * @param heightText the user's new height.
     * @param heightUnits the user's chosen units for height.
     * @return a boolean value representing whether the inputted values are suitable.
     */
    private boolean checkHeightFields(String heightText, String heightUnits)
    {
        //Both null, no action needed.
        if ((heightText == null || heightText.equals("")) && (heightUnits == null || heightUnits.equals(""))) {
            return false;
        }
        //Units selected but height not filled
        else if (heightText == null || heightText.equals("")) {
            heightLabel.setText("Please input height");
            return false;
        }
        //Height input as 0
        else if (Integer.parseInt(heightText) == 0) {
            heightLabel.setText("Height cannot be 0");
            return false;
        }
        //Height input correctly but units not selected
        else if (heightUnits == null || heightUnits.equals("")) {
            heightLabel.setText("Please select units");
            return false;
        }

        return true;
    }
}

