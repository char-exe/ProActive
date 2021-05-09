package Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.*;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A controller for the activity logging page of the app.
 *
 * @author Samuel Scarfe
 * @author Owen Tasker
 * @author Charlie Jones
 *
 * @version 1.7
 *
 * 1.0  - Initial commit, dummy file.
 * 1.1  - Implemented simple exercise logging to database.
 * 1.2  - Implemented simple weight logging to database.
 * 1.3  - Implemented simple food logging to database.
 * 1.4  - Implemented value checking for logging, preventing null values and future dates.
 * 1.5  - Implemented table view for added foods, with reference to
 *        https://medium.com/@keeptoo/adding-data-to-javafx-tableview-stepwise-df582acbae4f.
 *        General commenting.
 * 1.6  - Implemented goal updating.
 * 1.7  - Implemented ability to create custom exercise and food items
 * 1.8  - Corrected int casts to float casts, in line with the database.
 * 1.9  - Updated goal updating for wider range of nutritional goals. Encapsulated goal updating in a private method.
 * 1.10 - Fixed bug where exercises not listed in exercise table could be logged in activities table with id -1.
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
    @FXML private Label noCupsIndicator;

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
    private HashMap<NutritionItem, Integer> breakfast;
    private HashMap<NutritionItem, Integer> lunch;
    private HashMap<NutritionItem, Integer> snack;
    private HashMap<NutritionItem, Integer> dinner;

    //Custom Item button
    @FXML private Button addCustomItem;

    private DatabaseHandler dh;
    private User user;

    // Water intake buttons / indicator
    @FXML private Button minusCup;
    @FXML private Button addCup;
    private int noCups = 0;

    /**
     * Method to be ran after all FXML elements have been loaded, used to impose restrictions and populate these
     * elements
     *
     * @param url FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
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

        // Default date set as today
        foodEntryDate.setValue(LocalDate.now());

        addCup.setOnAction(e -> {
            noCups++;
            noCupsIndicator.setText(String.valueOf(noCups));
        });

        minusCup.setOnAction(e -> {
            if(noCups > 0){
                noCups--;
                noCupsIndicator.setText(String.valueOf(noCups));
            }
        });

        foodEntryDate.valueProperty().addListener(e -> {
            noCups = dh.getWaterIntakeInCups(user.getUsername(), foodEntryDate.getValue());
            noCupsIndicator.setText(String.valueOf(noCups));
        });

    }

    /**
     * Method to allow data to be passed into this scene from another
     *
     * @param user Takes in a user object
     */
    public void initData(User user) {
        this.user = user;

        noCups = dh.getWaterIntakeInCups(user.getUsername(), LocalDate.now());
        noCupsIndicator.setText(String.valueOf(noCups));
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
                dh.insertExercise(user.getUsername(), exercise, minutes); //Throws IllegalStateException
                ExerciseItem exerciseItem = dh.getExerciseItem(exercise);

                try {
                    user.updateGoals(Goal.Unit.valueOf(exercise.toUpperCase(Locale.ROOT)), minutes);
                }
                catch (IllegalArgumentException e) { //Exercise not in enum (i.e. custom exercise)
                    System.out.println("Caught attempted invalid goals update.");
                }

                user.updateGoals(Goal.Unit.EXERCISE, minutes);
                user.updateGoals(Goal.Unit.BURNED, exerciseItem.calculateBurn(minutes));
                exercisePopUp.setText(exercise + " for " + minutesText + " minutes added to database");
            }
            catch (SQLException e) {
                exercisePopUp.setText("Error adding " + exercise + " for " + minutesText + " minutes to database");
                e.printStackTrace();
            }
            catch (IllegalStateException e) { //Exercise not in table
                exercisePopUp.setText("Error adding " + exercise + " for " + minutesText + " minutes to database. " +
                                      "If this is a custom activity make sure to add it first.");
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
            NutritionItem nutritionItem = dh.getNutritionItem(food);
            //add to map
            switch (meal) {
                case "Breakfast":
                    if (breakfast.containsKey(nutritionItem)) {
                        breakfast.put(nutritionItem, breakfast.get(nutritionItem) + quantity); //Increment existing
                    }
                    else {
                        breakfast.put(nutritionItem, quantity); //Add new
                    }
                    break;
                case "Lunch":
                    if (lunch.containsKey(nutritionItem)) {
                        lunch.put(nutritionItem, lunch.get(nutritionItem) + quantity); //Increment existing
                    }
                    else {
                        lunch.put(nutritionItem, quantity); //Add new
                    }
                    break;
                case "Dinner":
                    if (dinner.containsKey(nutritionItem)) {
                        dinner.put(nutritionItem, dinner.get(nutritionItem) + quantity); //Increment existing
                    }
                    else {
                        dinner.put(nutritionItem, quantity); //Add new
                    }
                    break;
                case "Snacks":
                    if (snack.containsKey(nutritionItem)) {
                        snack.put(nutritionItem, snack.get(nutritionItem) + quantity); //Increment existing
                    }
                    else {
                        snack.put(nutritionItem, quantity); //Add new
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
            for (NutritionItem key : breakfast.keySet()) {
                try {
                    updateUserNutritionGoals(key, breakfast);

                    dh.addFoodEntry(user.getUsername(), "Breakfast", key.getName(), breakfast.get(key), date);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (NutritionItem key : lunch.keySet()) {
                try {
                    updateUserNutritionGoals(key, lunch);

                    dh.addFoodEntry(user.getUsername(), "Lunch", key.getName(), lunch.get(key), date);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (NutritionItem key : dinner.keySet()) {
                try {
                    updateUserNutritionGoals(key, dinner);

                    dh.addFoodEntry(user.getUsername(), "Dinner", key.getName(), dinner.get(key), date);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (NutritionItem key : snack.keySet()) {
                try {
                    updateUserNutritionGoals(key, snack);

                    dh.addFoodEntry(user.getUsername(), "Snack", key.getName(), snack.get(key), date);
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

            if(!(dh.getWaterIntakeInCups(user.getUsername(), date) == noCups)){

                dh.setWaterIntake(user.getUsername(), date, noCups);

            }

            //Empty tables
            setTableData();

            //Show success message to user.
            foodDateLabel.setText("Meals added to database on " + date);
        }
    }

    /**
     * Private helper method for updating a user's nutrition goals. Calls the user's update goals method for every
     * nutrition target value.
     *
     * @param key A NutritionItem in the mealMap.
     * @param mealMap A map storing NutritionItems against their quantity consumed.
     */
    private void updateUserNutritionGoals(NutritionItem key, HashMap<NutritionItem, Integer> mealMap) {
        user.updateGoals(Goal.Unit.CALORIES, (float) (mealMap.get(key) * key.getKcal()/100));
        user.updateGoals(Goal.Unit.PROTEIN, (float) (mealMap.get(key) * key.getProteinG()/100));
        user.updateGoals(Goal.Unit.CARBS, (float) (mealMap.get(key) * key.getCarbsG()/100));
        user.updateGoals(Goal.Unit.FIBRE, (float) (mealMap.get(key) * key.getFibreG()/100));
        user.updateGoals(Goal.Unit.SODIUM, (float) (mealMap.get(key) * key.getSodiumMg()/100));
        user.updateGoals(Goal.Unit.POTASSIUM, (float) (mealMap.get(key) * key.getPotassiumMg()/100));
        user.updateGoals(Goal.Unit.CALCIUM, (float) (mealMap.get(key) * key.getCalciumMg()/100));
        user.updateGoals(Goal.Unit.MAGNESIUM, (float) (mealMap.get(key) * key.getMagnesiumMg()/100));
        user.updateGoals(Goal.Unit.PHOSPHORUS, (float) (mealMap.get(key) * key.getPhosphorusMg()/100));
        user.updateGoals(Goal.Unit.IRON, (float) (mealMap.get(key) * key.getIronMg()/100));
        user.updateGoals(Goal.Unit.COPPER, (float) (mealMap.get(key) * key.getCopperMg()/100));
        user.updateGoals(Goal.Unit.ZINC, (float) (mealMap.get(key) * key.getZincMg()/100));
        user.updateGoals(Goal.Unit.CHLORIDE, (float) (mealMap.get(key) * key.getChlorideMg()/100));
        user.updateGoals(Goal.Unit.SELENIUM, (float) (mealMap.get(key) * key.getSeleniumUg()/100));
        user.updateGoals(Goal.Unit.IODINE, (float) (mealMap.get(key) * key.getIodineUg()/100));
        user.updateGoals(Goal.Unit.VITAMIN_A, (float) (mealMap.get(key) * key.getVitAUg()/100));
        user.updateGoals(Goal.Unit.VITAMIN_D, (float) (mealMap.get(key) * key.getVitDUg()/100));
        user.updateGoals(Goal.Unit.THIAMIN, (float) (mealMap.get(key) * key.getThiaminMg()/100));
        user.updateGoals(Goal.Unit.RIBOFLAVIN, (float) (mealMap.get(key) * key.getRiboflavinMg()/100));
        user.updateGoals(Goal.Unit.NIACIN, (float) (mealMap.get(key) * key.getNiacinMg()/100));
        user.updateGoals(Goal.Unit.VITAMIN_B6, (float) (mealMap.get(key) * key.getVitB6Mg()/100));
        user.updateGoals(Goal.Unit.VITAMIN_B12, (float) (mealMap.get(key) * key.getVitB12Ug()/100));
        user.updateGoals(Goal.Unit.FOLATE, (float) (mealMap.get(key) * key.getFolateUg()/100));
        user.updateGoals(Goal.Unit.VITAMIN_C, (float) (mealMap.get(key) * key.getVitCMg()/100));
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
        for (NutritionItem key : breakfast.keySet()) {
            breakfastRows.add(new FoodItem(key, breakfast.get(key)));
        }
        for (NutritionItem key : lunch.keySet()) {
            lunchRows.add(new FoodItem(key, lunch.get(key)));
        }
        for (NutritionItem key : dinner.keySet()) {
            dinnerRows.add(new FoodItem(key, dinner.get(key)));
        }
        for (NutritionItem key : snack.keySet()) {
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
        private SimpleStringProperty foodName;
        private SimpleDoubleProperty calories;

        /**
         * Constructs a table row comprised of food name and calories.
         * @param food the food as a NutritionItem.
         * @param quantity the amount consumed in grams.
         */
        public FoodItem(NutritionItem food, int quantity) {
            this.foodName = new SimpleStringProperty(food.getName());
            double kcal = food.getKcal(); //stored in the item as kcal per 100g
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

    /**
     * Method to handle custom nutrition item creation
     *
     * @throws IOException Throws an IOException whenever it is possible for a file to be missing
     */
    public void customNutritionItemButtonAction() throws IOException {
        Parent part = FXMLLoader.load(getClass().getResource("/FXML/CreateNutritionItem.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(part);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to handle custom exercise item creation
     *
     * @throws IOException Throws an IOException whenever it is possible for a file to be missing
     */
    public void customExerciseItemButtonAction() throws IOException {
        Parent part = FXMLLoader.load(getClass().getResource("/FXML/CreateExerciseItem.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(part);
        stage.setScene(scene);
        stage.show();
    }
}
