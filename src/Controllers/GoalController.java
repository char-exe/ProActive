package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.DatabaseHandler;
import sample.User;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

/**
 * A controller for the goals page of the app.
 *
 * @author Samuel Scarfe
 *
 */

public class GoalController implements Initializable {

    //Set Goals tab
    @FXML private TextField dietAmountField;
    @FXML private ComboBox<String> dietUnitSelect;
    @FXML private DatePicker dietDateField;
    @FXML private TextField calorieAmountField;
    @FXML private DatePicker calorieDateField;
    @FXML private ComboBox<String> exerciseSelect;
    @FXML private TextField exerciseMinutesField;
    @FXML private DatePicker exerciseDateField;

    private User user;
    private DatabaseHandler dh;

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

    public void initData(User user) {
        this.user = user;
    }
}
