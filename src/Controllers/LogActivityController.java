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
 */

public class LogActivityController implements Initializable {

    @FXML private TableView breakfastTable;
    @FXML private TableView lunchTable;
    @FXML private TableView dinnerTable;
    @FXML private TableView snackTable;
    @FXML private ComboBox<String> exerciseComboBox;
    @FXML private TextField exerciseMinutesField;
    @FXML private TextField weightField;
    @FXML private DatePicker weightDateField;

    private DatabaseHandler dh;
    private User user;

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

        dh = DatabaseHandler.getInstance();

        exerciseComboBox.getItems().addAll(dh.getExerciseNames());
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

        dh.insertWeightValue(user.getUsername(), weight, date);
    }
}
