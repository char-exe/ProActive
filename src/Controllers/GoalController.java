package Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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
 *
 * @version 1.2
 *
 * 1.0 - First working version. Functionality for adding goals implemented with simple error checking.
 * 1.1 - Implemented functionality for checking current and past goals.
 * 1.2 - Implemented automatic goal generation. Extended goal setting for vitamins and minerals.
 */

public class GoalController implements Initializable {

    @FXML private TabPane tabPane;

    //Our Goals For You tab
    @FXML private VBox ourGoalsVbox;

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
    @FXML private TableView<GoalItem> currentGoalsTable;
    @FXML private TableColumn<GoalItem, Float> currentTargetColumn;
    @FXML private TableColumn<GoalItem, String> currentUnitColumn;
    @FXML private TableColumn<GoalItem, Float> currentProgressColumn;
    @FXML private TableColumn<GoalItem, String> currentEndDateColumn;

    //Past Goals tab
    @FXML private TableView<GoalItem> pastGoalsTable;
    @FXML private TableColumn<GoalItem, Float> pastTargetColumn;
    @FXML private TableColumn<GoalItem, String> pastUnitColumn;
    @FXML private TableColumn<GoalItem, Float> pastProgressColumn;
    @FXML private TableColumn<GoalItem, String> pastEndDateColumn;
    @FXML private TableColumn<GoalItem, Boolean> pastCompletedColumn;

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
        dietAmountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("(\\d\\.\\d)*")) {
                    dietAmountField.setText(newValue.replaceAll("[^(\\d\\.\\d)]", ""));
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

        //Set tab selection action for Current Goals
        //https://stackoverflow.com/questions/43092588/how-to-perform-some-action-when-the-tab-is-selected-in-javafx-scene-builder
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>()
        {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1)
            {
                if ("Current Goals".equals(t1.getText()))
                {
                    loadCurrentGoals(new ActionEvent());
                }
            }
        });

        //Set tab selection action for Past Goals
        //https://stackoverflow.com/questions/43092588/how-to-perform-some-action-when-the-tab-is-selected-in-javafx-scene-builder
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>()
        {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1)
            {
                if ("Past Goals".equals(t1.getText()))
                {
                    loadPastGoals(new ActionEvent());
                }
            }
        });

        //Instantiate table placeholder texts
        currentGoalsTable.setPlaceholder(new Label("No current goals, set some in Set Goals"));
        pastGoalsTable.setPlaceholder(new Label("No past goals, view current goals in Current Goals"));

        //Instantiate table columns
        currentTargetColumn.setCellValueFactory(new PropertyValueFactory<>("Target"));
        currentUnitColumn.setCellValueFactory(new PropertyValueFactory<>("Unit"));
        currentProgressColumn.setCellValueFactory(new PropertyValueFactory<>("Progress"));
        currentEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        pastTargetColumn.setCellValueFactory(new PropertyValueFactory<>("Target"));
        pastUnitColumn.setCellValueFactory(new PropertyValueFactory<>("Unit"));
        pastProgressColumn.setCellValueFactory(new PropertyValueFactory<>("Progress"));
        pastEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        pastCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("Completed"));
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
     * Method to instantiate Our Goals for You tab. Assigns a row to each goal, with a button to accept the goal on
     * the row.
     */
    public void showSystemGoals() {
        //For goal in user's system goals
        for (SystemGoal systemGoal : user.getSystemGoals()) {

            //Add label and button for goal
            HBox hbox = new HBox();
            ourGoalsVbox.getChildren().add(hbox);
            hbox.getChildren().add(new Label(systemGoal.toString()));
            Button button = new Button();

            //Set button text based on goal status
            if (systemGoal.isAccepted()) {
                button.setText("Accepted");
            }
            else {
                button.setText("Click to accept");
            }

            //Set button action such that if the goal is not accepted it is added to the user's goals, set to
            //accepted, updated in the database, and then the button updated.
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    if (!systemGoal.isAccepted()) {
                        user.addGoal(new IndividualGoal(systemGoal));
                        systemGoal.setAccepted(true);
                        user.saveSystemGoals();
                        button.setText("Accepted");
                    }
                }
            };

            button.setOnAction(event);

            hbox.getChildren().add(button);
        }
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

            float amount = Float.parseFloat(amountText); //Need to check as a string first to check for empty input.

            Goal goal = new IndividualGoal(amount, nutrientsMap.get(unitsText), dateText);
            user.addGoal(goal);
            dietGoalLabel.setText("Goal added : " + goal);
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
            Goal goal = new IndividualGoal(amount, Goal.Unit.BURNED, dateText);
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
     * Method for loading a user's current goals into the current goals table.
     *
     * @param actionEvent a mouseclick on the current goals tab.
     */
    public void loadCurrentGoals(ActionEvent actionEvent) {
        //Create holder for the table rows
        ObservableList<GoalItem> goalRows = FXCollections.observableArrayList();

        //Load rows into holder
        for (Goal goal : user.getGoals()) {
            if (goal.isActive() && !goal.isCompleted()) { //If goal is active active and not completed
                goalRows.add(new GoalItem(goal));
            }
        }

        //Add rows to table
        currentGoalsTable.setItems(goalRows);
    }

    /**
     * Method for loading a user's past goals into the past goals table.
     *
     * @param actionEvent a mouseclick on the past goals tab.
     */
    public void loadPastGoals(ActionEvent actionEvent) {
        //Create holder for table rows
        ObservableList<GoalItem> goalRows = FXCollections.observableArrayList();

        //Load rows into holder
        for (Goal goal : user.getGoals()) {
            if (!goal.isActive() || goal.isCompleted()) { //if end date has passed for goal or goal is complete
                goalRows.add(new GoalItem(goal));
            }
        }

        //Add rows to table
        pastGoalsTable.setItems(goalRows);
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
         * @param goal The goal to be parsed as a goal item.
         */
        public GoalItem(Goal goal) {
            this.target = new SimpleFloatProperty(goal.getTarget());
            this.unit = new SimpleStringProperty(goal.getUnit().toString());
            this.progress = new SimpleFloatProperty(goal.getProgress());
            this.endDate = new SimpleStringProperty(goal.getEndDate().toString());
            this.completed = new SimpleBooleanProperty(goal.isCompleted());
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
