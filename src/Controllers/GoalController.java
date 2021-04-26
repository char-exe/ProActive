package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.DatabaseHandler;
import sample.Goal;
import sample.IndividualGoal;
import sample.User;

import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

/**
 * A controller for the goals page of the app.
 *
 * @author Samuel Scarfe
 *
 * @version 1.0
 *
 * 1.0 - First working version. Functionality for adding goals implemented with simple error checking.
 *
 */

public class GoalController implements Initializable {

    //Set Goals tab
    @FXML private TextField dietAmountField;
    @FXML private ComboBox<String> dietUnitSelect;
    @FXML private DatePicker dietDateField;
    @FXML private Label dietGoalLabel;
    @FXML private TextField calorieAmountField;
    @FXML private DatePicker calorieDateField;
    @FXML private Label calorieGoalLabel;
    @FXML private ComboBox<String> exerciseSelect;
    @FXML private TextField exerciseMinutesField;
    @FXML private DatePicker exerciseDateField;
    @FXML private Label exerciseGoalLabel;

    private User user;
    private DatabaseHandler dh;

    /**
     * Method to be called once all FXML elements have been loaded, combined with initData acts as a pseudo-constructor.
     *
     * @param url FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dh = DatabaseHandler.getInstance();

        //Set dateAmountField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        dietAmountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    dietAmountField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set calorieAmountField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        calorieAmountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    calorieAmountField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set exerciseMinutesField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        exerciseMinutesField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    exerciseMinutesField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Instantiate dropdowns
        dietUnitSelect.getItems().addAll("Calories", "Protein (g)");
        exerciseSelect.getItems().add("Any Exercise");
        exerciseSelect.getItems().addAll(dh.getExerciseNames());
    }

    /**
     * Method to be called to instantiate the necessary data for this controller.
     *
     * @param user the user currently logged in.
     */
    public void initData(User user) {
        this.user = user;
    }

    /**
     * Method for parsing information passed to diet fields and creating a user goal.
     *
     * @param actionEvent a button click
     */
    public void setDietGoal(ActionEvent actionEvent) {
        //Get field values
        String amountText = dietAmountField.getText();
        String unitsText = dietUnitSelect.getValue();
        LocalDate dateText = dietDateField.getValue();

        //Check field values
        if (checkDietInputs(amountText, unitsText, dateText)) {

            int amount = Integer.parseInt(amountText); //Need to check as a string first to check for empty input.

            //Create goal and present message to user
            if (unitsText.equals("Calories")) {
                Goal goal = new IndividualGoal(amount, Goal.Unit.CALORIES, dateText);
                user.addGoal(goal);
                dietGoalLabel.setText("Goal added : " + goal);
            }
            else if (unitsText.equals("Protein (g)")) {
                Goal goal = new IndividualGoal(amount, Goal.Unit.PROTEIN, dateText);
                user.addGoal(goal);
                dietGoalLabel.setText("Goal added : " + goal);
            }
        }
    }

    /**
     * Method for parsing information passed to calorie fields and creating a user goal.
     *
     * @param actionEvent a button click
     */
    public void setCaloriesGoal(ActionEvent actionEvent) {
        //Get fields
        String amountText = calorieAmountField.getText();
        LocalDate dateText = calorieDateField.getValue();

        //Check fields
        if (checkCalorieInputs(amountText, dateText)) {

            int amount = Integer.parseInt(amountText); //Need to check as a string first to check for empty input

            //Create goal and present message to user
            Goal goal = new IndividualGoal(amount, Goal.Unit.BURN, dateText);
            user.addGoal(goal);
            calorieGoalLabel.setText("Goal added : " + goal);
        }
    }

    /**
     * Method for parsing information passed to exercise fields and creating a user goal.
     *
     * @param actionEvent a button click
     */
    public void setExerciseGoal(ActionEvent actionEvent) {
        //Get fields
        String amountText = exerciseMinutesField.getText();
        String unitsText = exerciseSelect.getValue();
        LocalDate dateText = exerciseDateField.getValue();

        //Check fields
        if (checkExerciseInputs(amountText, unitsText, dateText)) {

            int amount = Integer.parseInt(amountText); //Need to check as a string first to check for empty input

            //Create goal and present message to user
            Goal goal;
            if (unitsText.equals("Any Exercise")) {
                goal = new IndividualGoal(amount, Goal.Unit.EXERCISE, dateText);
            }
            else {
                goal = new IndividualGoal(amount, Goal.Unit.valueOf(unitsText.toUpperCase(Locale.ROOT)), dateText);
            }
            user.addGoal(goal);
            exerciseGoalLabel.setText("Goal added : " + goal);
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
    private boolean checkDietInputs(String amount, String units, LocalDate date) {
        if (amount == null || amount.equals("")) { //Empty input
            dietGoalLabel.setText("Please enter an amount");
            return false;
        }
        else if (Integer.parseInt(amount) == 0) { //Zero input
            dietGoalLabel.setText("Amount cannot be 0");
            return false;
        }
        else if (units == null || units.equals("")) { //Units not selected
            dietGoalLabel.setText("Please select units");
            return false;
        }
        else if (date == null) { //Date not selected
            dietGoalLabel.setText("Please select a date");
            return false;
        }
        else if (!date.isAfter(LocalDate.now())) { //Date in past
            dietGoalLabel.setText("Date must be in the future");
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
    private boolean checkCalorieInputs(String amount, LocalDate date) {
        if (amount == null || amount.equals("")) { //Empty input
            calorieGoalLabel.setText("Please enter an amount");
            return false;
        }
        else if (Integer.parseInt(amount) == 0) { //Zero input
            calorieGoalLabel.setText("Amount cannot be 0");
            return false;
        }
        else if (date == null) { //Date not selected
            calorieGoalLabel.setText("Please select a date");
            return false;
        }
        else if (!date.isAfter(LocalDate.now())) { //Date in past
            calorieGoalLabel.setText("Date must be in the future");
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
    private boolean checkExerciseInputs(String amount, String units, LocalDate date) {
        if (amount == null || amount.equals("")) { //Empty input
            exerciseGoalLabel.setText("Please enter an amount");
            return false;
        }
        else if (Integer.parseInt(amount) == 0) { //Zero input
            exerciseGoalLabel.setText("Amount cannot be 0");
            return false;
        }
        else if (units == null || units.equals("")) { //Exercise not selected
            exerciseGoalLabel.setText("Please select an exercise");
            return false;
        }
        else if (date == null) { //Date not selected
            exerciseGoalLabel.setText("Please select a date");
            return false;
        }
        else if (!date.isAfter(LocalDate.now())) { //Date in past
            exerciseGoalLabel.setText("Date must be in the future");
            return false;
        }

        return true; //All field fine
    }
}
