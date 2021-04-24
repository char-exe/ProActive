package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.DatabaseHandler;
import sample.User;

import java.io.IOException;
import java.net.URL;
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
 * @version 1.2
 *
 * 1.0 - Initial commit, dummy file.
 * 1.1 - Implemented simple exercise logging to database.
 * 1.2 - Implemented simple weight logging to database.
 * 1.3 - Implemented simple food logging to database.
 */

public class LogActivityController implements Initializable {

    @FXML private TableView breakfastTable;
    @FXML private TableView lunchTable;
    @FXML private TableView dinnerTable;
    @FXML private TableView snackTable;
    @FXML private ComboBox<String> exerciseComboBox;
    @FXML private TextField exerciseMinutesField;
    @FXML private TextField weightField;
    @FXML private ChoiceBox<String> weightUnits;
    @FXML private DatePicker weightDateField;
    @FXML private ComboBox<String> mealSelect;
    @FXML private ComboBox<String> foodComboBox;
    @FXML private TextField foodQuantity;
    @FXML private DatePicker foodEntryDate;

    private DatabaseHandler dh;
    private User user;
    private HashMap<String, Integer> breakfast;
    private HashMap<String, Integer> lunch;
    private HashMap<String, Integer> snack;
    private HashMap<String, Integer> dinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

        weightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    weightField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        foodQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    foodQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        dh = DatabaseHandler.getInstance();

        breakfast = new HashMap<>();
        lunch = new HashMap<>();
        snack = new HashMap<>();
        dinner = new HashMap<>();

        exerciseComboBox.getItems().addAll(dh.getExerciseNames());
        weightUnits.getItems().add("kg");
        weightUnits.getItems().add("lbs");
        mealSelect.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snacks");
        foodComboBox.getItems().addAll(dh.getFoodNames());
        breakfastTable.setPlaceholder(new Label("Add food item or custom item"));
        lunchTable.setPlaceholder(new Label("Add food item or custom item"));
        dinnerTable.setPlaceholder(new Label("Add food item or custom item"));
        snackTable.setPlaceholder(new Label("Add food item or custom item"));

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
        String exercise = exerciseComboBox.getValue();
        int minutes = Integer.parseInt(exerciseMinutesField.getText());

        dh.insertExercise(user.getUsername(), exercise, minutes);
    }

    /**
     * Method for submitting a weight entry from the app to the database.
     *
     * @param actionEvent a mouseclick event on the submit button.
     */
    public void submitWeight(ActionEvent actionEvent) {
        float weight = Float.parseFloat(weightField.getText());
        LocalDate date = weightDateField.getValue();

        if (weightUnits.getValue() == "lbs") {
            weight = (float) (weight / 2.205);
        }

        dh.insertWeightValue(user.getUsername(), weight, date);
    }

    /**
     * Method to add individual food items to meals.
     *
     * @param actionEvent a mouseclick on the add button.
     */
    public void addFoodToMeal(ActionEvent actionEvent) {
        //get meal
        String meal = mealSelect.getValue();
        //get food
        String food = foodComboBox.getValue();
        //get quantity
        int quantity = Integer.parseInt(foodQuantity.getText());

        //add to map
        switch (meal)
        {
            case "Breakfast":
                if (breakfast.containsKey(food)) {
                    breakfast.put(food, breakfast.get(food) + quantity);
                }
                else {
                    breakfast.put(food, quantity);
                }
                break;
            case "Lunch":
                if (lunch.containsKey(food)) {
                    lunch.put(food, lunch.get(food) + quantity);
                }
                else {
                    lunch.put(food, quantity);
                }
                break;
            case "Dinner":
                if (dinner.containsKey(food)) {
                    dinner.put(food, dinner.get(food) + quantity);
                }
                else {
                    dinner.put(food, quantity);
                }
                break;
            case "Snacks":
                if (snack.containsKey(food)) {
                    snack.put(food, snack.get(food) + quantity);
                }
                else {
                    snack.put(food, quantity);
                }
                break;
        }

        System.out.println(food + " added to " + meal);
        //update table
    }

    /**
     * Method to add completed meals to the database.
     *
     * @param actionEvent a mouseclick on the save changes button.
     */
    public void submitFood(ActionEvent actionEvent) {
        //get date
        LocalDate date = foodEntryDate.getValue();
        //for each map
            //for each key
                //submit food entry
        for (String key : breakfast.keySet())
        {
            dh.addFoodEntry(user.getUsername(), "Breakfast", key, breakfast.get(key), date);
        }
        for (String key : lunch.keySet())
        {
            dh.addFoodEntry(user.getUsername(), "Lunch", key, lunch.get(key), date);
        }
        for (String key : dinner.keySet())
        {
            dh.addFoodEntry(user.getUsername(), "Dinner", key, dinner.get(key), date);
        }
        for (String key : snack.keySet())
        {
            dh.addFoodEntry(user.getUsername(), "Snack", key, snack.get(key), date);
        }

        //clear maps
        breakfast = new HashMap<>();
        lunch = new HashMap<>();
        dinner = new HashMap<>();
        snack = new HashMap<>();

        //update table
    }
}
