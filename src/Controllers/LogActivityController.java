package Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.DatabaseHandler;
import sample.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * A controller for the activity logging page of the app.
 *
 * @author Evan Clayton?
 * @author Samuel Scarfe
 *
 * @version 1.5
 *
 * 1.0 - Initial commit, dummy file.
 * 1.1 - Implemented simple exercise logging to database.
 * 1.2 - Implemented simple weight logging to database.
 * 1.3 - Implemented simple food logging to database.
 * 1.4 - Implemented value checking for logging, preventing null values and future dates.
 * 1.5 - Implemented table view for added foods, with reference to
 *       https://medium.com/@keeptoo/adding-data-to-javafx-tableview-stepwise-df582acbae4f.
 *       General commenting.
 */
public class LogActivityController implements Initializable {

    //Exercise tab fields
    @FXML private ComboBox<String> exerciseComboBox;
    @FXML private TextField exerciseMinutesField;
    @FXML private Label exercisePopUp;

    //Weight tab fields
    @FXML private TextField weightField;
    @FXML private ChoiceBox<String> weightUnits;
    @FXML private DatePicker weightDateField;
    @FXML private Label weightFieldsLabel;
    @FXML private Label weightDateLabel;

    //Food tab fields
    @FXML private ComboBox<String> mealSelect;
    @FXML private ComboBox<String> foodComboBox;
    @FXML private TextField foodQuantity;
    @FXML private DatePicker foodEntryDate;
    @FXML private Label foodFieldsLabel;
    @FXML private Label foodDateLabel;

    //Food tab tables
    @FXML private TableView<FoodItem> breakfastTable;
    @FXML private TableView<FoodItem> lunchTable;
    @FXML private TableView<FoodItem> dinnerTable;
    @FXML private TableView<FoodItem> snackTable;
    @FXML private TableColumn<FoodItem, String> breakfastFoodColumn;
    @FXML private TableColumn<FoodItem, Double> breakfastCaloriesColumn;
    @FXML private TableColumn<FoodItem, String> lunchFoodColumn;
    @FXML private TableColumn<FoodItem, Double> lunchCaloriesColumn;
    @FXML private TableColumn<FoodItem, String> dinnerFoodColumn;
    @FXML private TableColumn<FoodItem, Double> dinnerCaloriesColumn;
    @FXML private TableColumn<FoodItem, String> snacksFoodColumn;
    @FXML private TableColumn<FoodItem, Double> snacksCaloriesColumn;

    //Meal maps
    private HashMap<String, Integer> breakfast;
    private HashMap<String, Integer> lunch;
    private HashMap<String, Integer> snack;
    private HashMap<String, Integer> dinner;

    private DatabaseHandler dh;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

        //Set weightField to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        weightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    weightField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set foodQuantity to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        foodQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    foodQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Get DatabaseHandler instance
        dh = DatabaseHandler.getInstance();

        //Instantiate meal maps
        breakfast = new HashMap<>();
        lunch = new HashMap<>();
        snack = new HashMap<>();
        dinner = new HashMap<>();

        //Instantiate dropdowns
        exerciseComboBox.getItems().addAll(dh.getExerciseNames());
        weightUnits.getItems().addAll("kg", "lbs");
        mealSelect.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snacks");
        foodComboBox.getItems().addAll(dh.getFoodNames());

        //Instantiate table placeholder texts
        breakfastTable.setPlaceholder(new Label("Add food item or custom item"));
        lunchTable.setPlaceholder(new Label("Add food item or custom item"));
        dinnerTable.setPlaceholder(new Label("Add food item or custom item"));
        snackTable.setPlaceholder(new Label("Add food item or custom item"));

        //Instantiate table columns
        breakfastFoodColumn.setCellValueFactory(new PropertyValueFactory<>("FoodName"));
        breakfastCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("Calories"));
        lunchFoodColumn.setCellValueFactory(new PropertyValueFactory<>("FoodName"));
        lunchCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("Calories"));
        dinnerFoodColumn.setCellValueFactory(new PropertyValueFactory<>("FoodName"));
        dinnerCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("Calories"));
        snacksFoodColumn.setCellValueFactory(new PropertyValueFactory<>("FoodName"));
        snacksCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("Calories"));
    }

    /**
     * Method to allow data to be passed into this scene from another
     *
     * @param user Takes in a user object
     */
    public void initData(User user) {
        this.user = user;
    }

    /**
     * Method for submitting an exercise entry from the app to the database.
     *
     * @param actionEvent a mouseclick event on the submit button.
     */
    public void submitExercise(ActionEvent actionEvent) {
        //Empty message to user
        exercisePopUp.setText("");

        //Get user inputs
        String exercise = exerciseComboBox.getValue();
        String minutesText = (exerciseMinutesField.getText());

        if (checkExerciseFields(exercise, minutesText)) {
            int minutes = Integer.parseInt(minutesText); //Convert after checking as empty string needs to be checked

            //Try to add to database and show appropriate success/fail message to user
            try {
                dh.insertExercise(user.getUsername(), exercise, minutes);
                exercisePopUp.setText(exercise + " for " + minutesText + " minutes added to database");
            }
            catch (SQLException e) {
                exercisePopUp.setText("Error adding " + exercise + " for " + minutesText + " minutes to database");
                e.printStackTrace();
            }
        }
    }

    /**
     * Private helper method for checking input values to exercise fields and setting a label.
     *
     * @param exercise the exercise value entered.
     * @param minutesText the minutes value entered.
     * @return a boolean representing whether the fields have been correctly completed.
     */
    private boolean checkExerciseFields(String exercise, String minutesText) {
        if (exercise == null || exercise.equals("")) { //No exercise entered
            exercisePopUp.setText("Please enter an exercise");
            return false;
        }
        else if (minutesText == null || minutesText.equals("")) { //No minutes entered
            exercisePopUp.setText("Please enter how many minutes for");
            return false;
        }
        else if (Integer.parseInt(minutesText) == 0) { //0 entered for minutes
            exercisePopUp.setText("Minutes must be greater than 0");
            return false;
        }
        return true;
    }

    /**
     * Method for submitting a weight entry from the app to the database.
     *
     * @param actionEvent a mouseclick event on the submit button.
     */
    public void submitWeight(ActionEvent actionEvent) {
        //Empty messages to user
        weightFieldsLabel.setText("");
        weightDateLabel.setText("");

        //Get user inputs
        String weightText = weightField.getText();
        String weightUnit = weightUnits.getValue();
        LocalDate date = weightDateField.getValue();

        if (checkWeightFields(weightText, weightUnit, date)) {
            float weight = Float.parseFloat(weightText); //Convert after checking as empty string needs to be checked
            if (weightUnit.equals("lbs")) { //Convert to lbs
                weight = (float) (weight / 2.205);
            }

            //Try to add to database and show appropriate success/fail message
            try {
                dh.insertWeightValue(user.getUsername(), weight, date);
                weightFieldsLabel.setText(weightText + " added to database on " + date);
            }
            catch (SQLException e) {
                weightFieldsLabel.setText("Error adding " + weightText + " to database on " + date);
                e.printStackTrace();
            }
        }
    }

    /**
     * Private helper method for checking weight entry input values.
     *
     * @param weightText the weight value entered.
     * @param weightUnit the weight unit entered.
     * @param date the entry date entered.
     * @return a boolean value representing whether the update was successful.
     */
    private boolean checkWeightFields(String weightText, String weightUnit, LocalDate date) {
        if (weightText == null || weightText.equals("")) { //No weight entered
            weightFieldsLabel.setText("Please enter a weight value");
            return false;
        }
        else if (Float.parseFloat(weightText) == 0) { //0 weight entered
            weightFieldsLabel.setText("Weight cannot be 0");
            return false;
        }
        else if (weightUnit == null || weightUnit.equals("")) { //No unit selected
            weightFieldsLabel.setText("Please select a unit");
            return false;
        }
        else if (date == null) { //No date selected
            weightDateLabel.setText("Please select a date");
            return false;
        }
        else if (date.isAfter(LocalDate.now())) { //Future date selected
            weightDateLabel.setText("Date cannot be in the future");
            return false;
        }

        return true;
    }

    /**
     * Method to add individual food items to meals.
     *
     * @param actionEvent a mouseclick on the add button.
     */
    public void addFoodToMeal(ActionEvent actionEvent) {
        //Empty messages to user
        foodFieldsLabel.setText("");
        foodDateLabel.setText("");

        //Get user inputs
        String meal = mealSelect.getValue();
        String food = foodComboBox.getValue();
        String quantityText = foodQuantity.getText();

        if (checkFoodFields(meal, food, quantityText)) {
            int quantity = Integer.parseInt(quantityText); //Convert after checking as empty string needs to be checked
            //add to map
            switch (meal) {
                case "Breakfast":
                    if (breakfast.containsKey(food)) {
                        breakfast.put(food, breakfast.get(food) + quantity); //Increment existing
                    } else {
                        breakfast.put(food, quantity); //Add new
                    }
                    break;
                case "Lunch":
                    if (lunch.containsKey(food)) {
                        lunch.put(food, lunch.get(food) + quantity); //Increment existing
                    } else {
                        lunch.put(food, quantity); //Add new
                    }
                    break;
                case "Dinner":
                    if (dinner.containsKey(food)) {
                        dinner.put(food, dinner.get(food) + quantity); //Increment existing
                    } else {
                        dinner.put(food, quantity); //Add new
                    }
                    break;
                case "Snacks":
                    if (snack.containsKey(food)) {
                        snack.put(food, snack.get(food) + quantity); //Increment existing
                    } else {
                        snack.put(food, quantity); //Add new
                    }
                    break;
            }

            //Show success message to user
            System.out.println(food + " added to " + meal);
            foodFieldsLabel.setText(food + " added to " + meal);

            //update table
            setTableData();
        }
    }

    /**
     * Private helper method for checking the input values for adding foods to meals.
     *
     * @param meal the meal selected.
     * @param food the food entered.
     * @param quantityText the quantity consumed.
     * @return a boolean value representing whether the input was successful.
     */
    private boolean checkFoodFields(String meal, String food, String quantityText) {
        if (meal == null || meal.equals("")) { //No meal entered
            foodFieldsLabel.setText("Please select a valid meal");
            return false;
        }
        else if (food == null || food.equals("")) { //No food entered
            foodFieldsLabel.setText("Please select a valid food");
            return false;
        }
        else if (quantityText == null || quantityText.equals("")) { //No quantity entered
            foodFieldsLabel.setText("Please enter an amount consumed");
            return false;
        }
        else if (Integer.parseInt(quantityText) == 0) { //0 entered for quantity
            foodFieldsLabel.setText("Quantity cannot be 0");
            return false;
        }

        return true;
    }

    /**
     * Method to add completed meals to the database.
     *
     * @param actionEvent a mouseclick on the save changes button.
     */
    public void submitFood(ActionEvent actionEvent) {
        //Empty messages to user
        foodFieldsLabel.setText("");
        foodDateLabel.setText("");

        //Get user input
        LocalDate date = foodEntryDate.getValue();

        if (checkFoodDate(date)) {
            //For each map
                //For each key
                    //Submit food entry to database
            for (String key : breakfast.keySet()) {
                try {
                    dh.addFoodEntry(user.getUsername(), "Breakfast", key, breakfast.get(key), date);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (String key : lunch.keySet()) {
                try {
                dh.addFoodEntry(user.getUsername(), "Lunch", key, lunch.get(key), date);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (String key : dinner.keySet()) {
                try {
                dh.addFoodEntry(user.getUsername(), "Dinner", key, dinner.get(key), date);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (String key : snack.keySet()) {
                try {
                dh.addFoodEntry(user.getUsername(), "Snack", key, snack.get(key), date);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //Clear maps.
            breakfast = new HashMap<>();
            lunch = new HashMap<>();
            dinner = new HashMap<>();
            snack = new HashMap<>();

            //Empty tables
            setTableData();

            //Show success message to user.
            foodDateLabel.setText("Meals added to database on " + date);
        }
    }

    /**
     * Private helper method for checking whether the date value for adding a food value is suitable.
     *
     * @param date the date entered.
     * @return a boolean value representing whether the update was successful.
     */
    private boolean checkFoodDate(LocalDate date) {
        if (date == null) { //No date selected
            foodDateLabel.setText("Please enter a date");
            return false;
        }
        else if (date.isAfter(LocalDate.now())) { //Future date selected
            foodDateLabel.setText("Date cannot be in the future");
            return false;
        }

        return true;
    }

    /**
     * Method to set the table contents on each of the meal tabs to the content of their respective
     * maps.
     */
    public void setTableData() {
        //Create data holders for the tables
        ObservableList<FoodItem> breakfastRows = FXCollections.observableArrayList();
        ObservableList<FoodItem> lunchRows = FXCollections.observableArrayList();
        ObservableList<FoodItem> dinnerRows = FXCollections.observableArrayList();
        ObservableList<FoodItem> snacksRows = FXCollections.observableArrayList();

        //Add each key to its holder with its calorie amount
        for (String key : breakfast.keySet()) {
            breakfastRows.add(new FoodItem(key, breakfast.get(key)));
        }
        for (String key : lunch.keySet()) {
            lunchRows.add(new FoodItem(key, lunch.get(key)));
        }
        for (String key : dinner.keySet()) {
            dinnerRows.add(new FoodItem(key, dinner.get(key)));
        }
        for (String key : snack.keySet()) {
            snacksRows.add(new FoodItem(key, snack.get(key)));
        }

        //Add data to the tables
        breakfastTable.setItems(breakfastRows);
        lunchTable.setItems(lunchRows);
        dinnerTable.setItems(dinnerRows);
        snackTable.setItems(snacksRows);
    }

    /**
     * Wrapper class for table rows, wraps food name and calories into one class.
     */
    public class FoodItem {
        SimpleStringProperty foodName;
        SimpleDoubleProperty calories;

        /**
         * Constructs a table row comprised of food name and calories.
         * @param foodName the name of the food.
         * @param quantity the amount consumed in grams.
         */
        FoodItem(String foodName, int quantity) {
            this.foodName = new SimpleStringProperty(foodName);
            double kcal = dh.getKcal(foodName); //stored in the database as kcal per 100g
            calories = new SimpleDoubleProperty(((kcal * quantity) / 100));
        }

        /**
         * Gets the food name for this table row.
         *
         * @return the food name for this table row.
         */
        public String getFoodName() {
            return foodName.get();
        }

        /**
         * Sets the food name for this table row.
         *
         * @param foodName the food name for this table row.
         */
        public void setFoodName(String foodName) {
            this.foodName = new SimpleStringProperty(foodName);
        }

        /**
         * Gets the calories value for this table row.
         *
         * @return the calories value for this table row.
         */
        public Double getCalories() {
            return calories.get();
        }

        /**
         * Sets the calories value for this table row.
         *
         * @param calories the calories value for this table row.
         */
        public void setCalories(double calories) {
            this.calories = new SimpleDoubleProperty(calories);
        }
    }
}
