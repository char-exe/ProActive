package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.DatabaseHandler;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Class to allow for the creation of custom exercise items
 *
 * @author Owen Tasker
 *
 * @version 1.0
 */
public class CreateExerciseItemController implements Initializable {
    @FXML public TextArea nameInput;
    @FXML public TextArea caloricBurnInput;
    @FXML public Button submitButton;

    @FXML public Label nameInputPopUp;
    @FXML public Label caloricBurnPopUp;


    /**
     * Method to run after all FXML elements have been loaded, used for imposing restrictions on FXML elements
     *
     * @param url FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set caloric burn to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        caloricBurnInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    caloricBurnInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * Method to handle submit button action
     *
     * @throws SQLException Throws an SQLException whenever it is possible that am external force could modify
     *                      SQL result
     */
    @FXML public void submitButtonAction() throws SQLException {
        DatabaseHandler dbh = DatabaseHandler.getInstance();

        if (checkInputs()){
            Stage parentScene = (Stage) submitButton.getScene().getWindow();
            dbh.addExerciseItem(nameInput.getText(),
                                Integer.parseInt(caloricBurnInput.getText()));

            System.out.println("Item Created: " + nameInput.getText());

            parentScene.close();

        }
    }

    /**
     * Method to check all inputs match correctly
     *
     * @return True if all checks are true, false otherwise
     */
    public boolean checkInputs() {
        return checkFirstName() && checkBurnRateInput();
    }

    /**
     * Method to check name field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFirstName() {
        String name = nameInput.getText();
        //Regex for nameInput textField
        String NAMEINPUTREGEX = "[a-zA-Z]*";
        if (name.matches(NAMEINPUTREGEX)) {
            nameInputPopUp.setText("");
            return true;
        }
        else {
            nameInputPopUp.setText("Please enter a valid Name");
            return false;
        }
    }

    /**
     * Method to check burn rate field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkBurnRateInput() {
        String burnRate = caloricBurnInput.getText();
        //https://www.regular-expressions.info/floatingpoint.html
        //Regex for inputfields that will take doubles
        String INTEGERINPUTREGEX = "[0-9]+";
        if (burnRate.matches(INTEGERINPUTREGEX)) {
            caloricBurnPopUp.setText("");
            return true;
        }
        else {
            caloricBurnInput.setText("Please enter a valid value");
            return false;
        }
    }
}
