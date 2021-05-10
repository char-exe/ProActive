package Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import sample.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

/**
 * A controller for the goals page of the app.
 *
 * @author Samuel Scarfe
 * @author Evan Clayton
 *
 * @version 1.4
 *
 * 1.0 - First working version. Functionality for adding goals implemented with simple error checking.
 * 1.1 - Implemented functionality for checking current and past goals.
 * 1.2 - Implemented automatic goal generation. Extended goal setting for vitamins and minerals.
 * 1.3 - Added goal management. Updated styling. Removed now superfluous inner class GoalItem.
 * 1.4 - Added functionality for group goals tab.
 */

public class GoalController implements Initializable {

    @FXML private TabPane tabPane;

    //Our Goals For You tab
    @FXML private VBox ourGoalsVbox;
    @FXML private ChoiceBox<String> ourGoalsDropDown;

    //Group Goals tab
    @FXML private VBox groupGoalsVbox;

    //Set Your Own tab
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

    //Current Goals tab
    @FXML private VBox currentGoalsVbox;

    //Past Goals tab
    @FXML private VBox completedGoalsVbox;

    private User user;
    private DatabaseHandler dh;

    private static HashMap<String, Goal.Unit> nutrientsMap;

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
        dietAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d\\.\\d)*")) {
                dietAmountField.setText(newValue.replaceAll("[^(\\d\\.\\d)]", ""));
            }
        });

        //Set calorieAmountField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        calorieAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                calorieAmountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //Set exerciseMinutesField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        exerciseMinutesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                exerciseMinutesField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

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

        //Instantiate dropdowns
        dietUnitSelect.getItems().addAll(nutrientsMap.keySet());
        exerciseSelect.getItems().add("Any Exercise");
        exerciseSelect.getItems().addAll(dh.getExerciseNames());
        ourGoalsDropDown.getItems().addAll("All Goals", "Fitness", "Nutrition");
        ourGoalsDropDown.setValue("All Goals");

        //Set ourGoalsDropDown actions
        ourGoalsDropDown.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if ("All Goals".equals(t1)) {
                ourGoalsVbox.getChildren().clear();
                showSystemGoals();
            }
        });

        ourGoalsDropDown.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if ("Fitness".equals(t1)) {
                ourGoalsVbox.getChildren().clear();
                showFitnessGoals(0);
            }
        });

        ourGoalsDropDown.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if ("Nutrition".equals(t1)) {
                ourGoalsVbox.getChildren().clear();
                showNutritionGoals(0);
            }
        });

        //Set tab selection action for Current Goals
        //https://stackoverflow.com/questions/43092588/how-to-perform-some-action-when-the-tab-is-selected-in-javafx-scene-builder
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if ("Current Goals".equals(t1.getText()))
            {
                loadCurrentGoals();
            }
        });

        //Set tab selection action for Past Goals
        //https://stackoverflow.com/questions/43092588/how-to-perform-some-action-when-the-tab-is-selected-in-javafx-scene-builder
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, t, t1) -> {
            if ("Completed Goals".equals(t1.getText()))
            {
                loadPastGoals();
            }
        });
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
     * Method to instantiate all goals for the Our Goals for You tab.
     */
    public void showSystemGoals() {
        int stylesIndex = showFitnessGoals(0);
        showNutritionGoals(stylesIndex);
    }

    /**
     * Method to instantiate only fitness goals for the Our Goals for You tab.
     *
     * @param stylesIndex the row index modulo two, to allow alternating styles
     */
    public int showFitnessGoals(int stylesIndex) {

        //Show goals marked as Stay on Pace
        Label stay = new Label("Stay on pace with...");
        ourGoalsVbox.getChildren().add(stay);

        for (SystemGoal systemGoal : user.getSystemGoals()) {
            if (systemGoal.getCategory() == SystemGoal.Category.STAY) {
                stylesIndex = showGoal(systemGoal, stylesIndex);
            }
        }

        //Show goals marked as Push Harder
        Label push = new Label("Push harder with...");
        ourGoalsVbox.getChildren().add(push);

        for (SystemGoal systemGoal : user.getSystemGoals()) {
            if (systemGoal.getCategory() == SystemGoal.Category.PUSH) {
                stylesIndex = showGoal(systemGoal, stylesIndex);
            }
        }

        return stylesIndex;
    }

    /**
     * Method to instantiate only nutrition goals for the Our Goals for You tab.
     *
     * @param stylesIndex the row index modulo two, to allow alternating styles
     */
    public void showNutritionGoals(int stylesIndex) {

        Label rdi = new Label("Hit your recommended daily intake with...");

        ourGoalsVbox.getChildren().add(rdi);
        rdi.getStyleClass().add("ourGoalsVbox");

        //For goal in user's system goals
        for (SystemGoal systemGoal : user.getSystemGoals()) {
            if (systemGoal.getCategory() == SystemGoal.Category.DAY_TO_DAY) {

                stylesIndex = showGoal(systemGoal, stylesIndex);
            }
        }
    }

    /**
     * Private helper method to instantiate a row for one goal.
     *
     * @param systemGoal the goal to be instantiated as a row
     * @param stylesIndex the index of the row modulo 2
     * @return the index of the next row modulo 2
     */
    private int showGoal(SystemGoal systemGoal, int stylesIndex)
    {
        String[] styles = {"goalsHboxOdd", "goalsHboxEven"}; //Alternating style classes


        //Create label such that label container fills all space available and is centered within.
        Region region1 = new Region();
        Region region2 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        HBox.setHgrow(region2, Priority.ALWAYS);
        Label label = new Label(systemGoal.toString());
        HBox innerBox = new HBox(region1, label, region2);
        HBox.setHgrow(innerBox, Priority.ALWAYS);
        innerBox.setAlignment(Pos.CENTER);

        //Create button
        Button button = new Button();

        //Set button text based on goal status
        if (systemGoal.isAccepted()) {
            button.setText("Accepted");
        } else {
            button.setText("Click to accept");
        }

        button.setMinWidth(125); //Width set such that it doesn't change when text changes

        //Set button action
        button.setOnAction(e -> {
            if (!systemGoal.isAccepted()) { //If goal not labelled accepted
                user.addGoal(new IndividualGoal(systemGoal)); //Add to the user's individual goals
                systemGoal.setAccepted(true); //Mark accepted
                user.saveSystemGoals(); //Save user's goals in the database
                button.setText("Accepted"); //Update button text
            }
        });

        //Add button and label to an HBox with content centered, CSS styled, and margins set
        HBox hbox = new HBox(innerBox, button);
        hbox.getStyleClass().add(styles[stylesIndex]);
        hbox.setAlignment(Pos.CENTER);
        VBox.setMargin(hbox, new Insets(5, 5, 5, 5));

        ourGoalsVbox.getChildren().add(hbox);

        stylesIndex = ++stylesIndex%2; //Increment then mod 2

        return stylesIndex;
    }

    /**
     * Method for parsing information passed to diet fields and creating a user goal.
     */
    public void setDietGoal() {
        //Get field values
        String amountText = dietAmountField.getText();
        String unitsText = dietUnitSelect.getValue();
        LocalDate dateText = dietDateField.getValue();

        //Check field values
        if (checkDietInputs(amountText, unitsText, dateText)) {

            float amount = Float.parseFloat(amountText); //Need to check as a string first to check for empty input.

            IndividualGoal goal = new IndividualGoal(amount, nutrientsMap.get(unitsText), dateText);
            user.addGoal(goal);
            dietGoalLabel.setText("Goal added : " + goal);
        }
    }

    /**
     * Method for parsing information passed to calorie fields and creating a user goal.
     */
    public void setCaloriesGoal() {
        //Get fields
        String amountText = calorieAmountField.getText();
        LocalDate dateText = calorieDateField.getValue();

        //Check fields
        if (checkCalorieInputs(amountText, dateText)) {

            int amount = Integer.parseInt(amountText); //Need to check as a string first to check for empty input

            //Create goal and present message to user
            IndividualGoal goal = new IndividualGoal(amount, Goal.Unit.BURNED, dateText);
            user.addGoal(goal);
            calorieGoalLabel.setText("Goal added : " + goal);
        }
    }

    /**
     * Method for parsing information passed to exercise fields and creating a user goal.
     */
    public void setExerciseGoal() {
        //Get fields
        String amountText = exerciseMinutesField.getText();
        String unitsText = exerciseSelect.getValue();
        LocalDate dateText = exerciseDateField.getValue();

        //Check fields
        if (checkExerciseInputs(amountText, unitsText, dateText)) {

            int amount = Integer.parseInt(amountText); //Need to check as a string first to check for empty input

            //Create goal and present message to user
            IndividualGoal goal;
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
     * Method for loading a user's current goals into the current goals table.
     */
    public void loadCurrentGoals() {

        //Empty current goals container
        currentGoalsVbox.getChildren().clear();

        int stylesIndex = 0; //Index for alternating styles
        int count = 0; // Count rows presented

        //For every goal
        for (UserGoal goal : user.getGoals()) {
            boolean isActive = false;
            boolean isCompleted = false;

            if (goal instanceof IndividualGoal) {
                isActive = ((IndividualGoal) goal).isActive();
                isCompleted = ((IndividualGoal) goal).isCompleted();
            }

            if (isActive && !isCompleted) { //If goal is active active and not completed
                count++;

                String[] styles = {"goalsHboxOdd", "goalsHboxEven"}; //Alternating style classes

                //Create a column of labels such that the column is centered and the surrounding container
                //fills all available space.
                Region region1 = new Region();
                Region region2 = new Region();
                HBox.setHgrow(region1, Priority.ALWAYS);
                HBox.setHgrow(region2, Priority.ALWAYS);
                Label goalLabel = new Label(goal.toString());
                Label progressLabel = new Label("Progress : " + goal.getProgress());
                VBox innerVBox = new VBox(goalLabel, progressLabel);
                HBox.setHgrow(innerVBox, Priority.ALWAYS);
                innerVBox.setAlignment(Pos.CENTER);
                HBox innerHBox = new HBox(region1, innerVBox, region2);
                HBox.setHgrow(innerHBox, Priority.ALWAYS);
                innerHBox.setAlignment(Pos.CENTER);


                //Create button
                Button button = new Button();
                button.setText("Quit Goal");
                button.setMinWidth(125); //Width set such that it doesn't change when text changes

                //Set button action
                button.setOnAction(e -> {
                    user.quitGoal(goal);
                    loadCurrentGoals(); //Reload goals
                });

                //Add button and labels to an HBox with content centered, CSS styled, and margins set
                HBox hbox = new HBox(innerHBox, button);
                hbox.getStyleClass().add(styles[stylesIndex]);
                hbox.setAlignment(Pos.CENTER);
                VBox.setMargin(hbox, new Insets(5, 5, 5, 5));

                currentGoalsVbox.getChildren().add(hbox);

                stylesIndex = ++stylesIndex%2; //Increment then mod 2
            }
        }

        if (count == 0) { //No goals presented
            Label label = new Label("No Current Goals, take a look at our suggestions in " +
                    "Our Goals For You or set your own in Set Your Own");
            label.setWrapText(true);
            label.getStyleClass().add("centerLabel");
            currentGoalsVbox.getChildren().add(label);
        }
    }

    /**
     * Method for loading a user's past goals into the past goals table.
     */
    public void loadPastGoals() {

        completedGoalsVbox.getChildren().clear();

        int stylesIndex = 0;
        int count = 0;

        //Load rows into holder
        for (Goal goal : user.getGoals()) {
            boolean isCompleted = false;

            if (goal instanceof IndividualGoal) {
                isCompleted = ((IndividualGoal) goal).isCompleted();
            }

            if (isCompleted) { //if goal is complete
                count++;
                String[] styles = {"goalsHboxOdd", "goalsHboxEven"}; //Alternating style classes

                //Create an HBox for each goal containing a centered label
                Region region1 = new Region();
                Region region2 = new Region();
                HBox.setHgrow(region1, Priority.ALWAYS);
                HBox.setHgrow(region2, Priority.ALWAYS);
                Label goalLabel = new Label(goal.toString());
                HBox hbox = new HBox(region1, goalLabel, region2);
                HBox.setHgrow(hbox, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER);

                //Style the HBox and add to the parent container.
                hbox.getStyleClass().add(styles[stylesIndex]);
                VBox.setMargin(hbox, new Insets(5, 5, 5, 5));

                completedGoalsVbox.getChildren().add(hbox);

                stylesIndex = ++stylesIndex%2; //Increment then mod 2
            }
        }

        if (count == 0) { //No goals presented

            Label label = new Label("No Completed Goals, take a look at your current goals in Current Goals");
            label.setWrapText(true);
            label.getStyleClass().add("centerLabel");
            completedGoalsVbox.getChildren().add(label);
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

        return true; //All fields fine
    }

    /**
     * Wrapper class for table rows, wraps target, unit, progress, end date, and completed status into a holding class.
     */
    public class GoalItem {
        private SimpleFloatProperty target;
        private SimpleStringProperty unit;
        private SimpleFloatProperty progress;
        private SimpleStringProperty endDate;
        private SimpleBooleanProperty completed;

        /**
         * Constructs a goal item from a goal.
         *
         * @param userGoal The goal to be parsed as a goal item.
         */
        public GoalItem(UserGoal userGoal) {
            this.target = new SimpleFloatProperty(userGoal.getTarget());
            this.unit = new SimpleStringProperty(userGoal.getUnit().toString());
            this.progress = new SimpleFloatProperty(userGoal.getProgress());
            this.endDate = new SimpleStringProperty(userGoal.getEndDate().toString());
            this.completed = new SimpleBooleanProperty(userGoal.isCompleted());
        }

        /**
         * Gets the target for this goal item.
         *
         * @return the target for this goal item.
         */
        public Float getTarget() {
            return this.target.get();
        }

        /**
         * Sets the target for this goal item to the passed value.
         *
         * @param target the new target value.
         */
        public void setTarget(int target) {
            this.target = new SimpleFloatProperty(target);
        }

        /**
         * Gets the unit for this goal item.
         *
         * @return the nit for this goal item.
         */
        public String getUnit() {
            return this.unit.get();
        }

        /**
         * Sets the unit for this goal item to the passed value.
         *
         * @param unit the new unit value.
         */
        public void setUnit(String unit) {
            this.unit = new SimpleStringProperty(unit);
        }

        /**
         * Gets the progress for this goal item.
         *
         * @return the progress for this goal item.
         */
        public Float getProgress() {
            return this.progress.get();
        }

        /**
         * Sets the progress for this goal item to the passed value.
         *
         * @param progress the new progress value.
         */
        public void setProgress(float progress) {
            this.progress = new SimpleFloatProperty(progress);
        }

        /**
         * Gets the end date for this goal item.
         *
         * @return the end date for this goal item.
         */
        public String getEndDate() {
            return this.endDate.get();
        }

        /**
         * Sets the end date for this goal item to the passed value.
         *
         * @param endDate the new end date value.
         */
        public void setEndDate(String endDate) {
            this.endDate = new SimpleStringProperty(endDate);
        }

        /**
         * Gets the completed status for this goal item.
         *
         * @return the completed status for this goal item.
         */
        public Boolean getCompleted() {
            return this.completed.get();
        }

        /**
         * Sets the completed status for this goal item to the passed value.
         *
         * @param completed the new completed status value.
         */
        public void setCompleted(boolean completed) {
            this.completed = new SimpleBooleanProperty(completed);
        }
    }
}
