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
import java.util.ResourceBundle;

public class LogActivityController implements Initializable {

    @FXML private TableView breakfastTable;
    @FXML private TableView lunchTable;
    @FXML private TableView dinnerTable;
    @FXML private TableView snackTable;
    @FXML private ComboBox<String> exerciseComboBox;
    @FXML private TextField exerciseMinutesField;
    @FXML private Button exerciseSubmit;

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

    public void submitExercise(ActionEvent actionEvent) {
        String exercise = exerciseComboBox.getValue();
        int minutes = Integer.parseInt(exerciseMinutesField.getText());

        dh.insertExercise(user.getUsername(), exercise, minutes);
    }
}
