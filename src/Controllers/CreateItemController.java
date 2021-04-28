package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.DatabaseHandler;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateItemController implements Initializable {
    @FXML public TextArea nameInput;
    @FXML public TextArea kcalInput;
    @FXML public TextArea proteinInput;
    @FXML public TextArea fatInput;
    @FXML public TextArea carbsInput;
    @FXML public TextArea sugarInput;
    @FXML public TextArea fibreInput;
    @FXML public TextArea cholesterolInput;
    @FXML public Button submitButton;

    @FXML public Label nameInputPopUp;
    @FXML public Label kcalInputPopUp;
    @FXML public Label proteinInputPopUp;
    @FXML public Label fatInputPopUp;
    @FXML public Label carbsInputPopUp;
    @FXML public Label sugarInputPopUp;
    @FXML public Label fibreInputPopUp;
    @FXML public Label cholesterolInputPopUp;
    

    //Regex for nameInput textField
    private final String NAMEINPUTREGEX = "[a-zA-Z]*";

    //https://www.regular-expressions.info/floatingpoint.html
    //Regex for inputfields that will take doubles
    private final String DOUBLEINPUTREGEX = "[-+]?[0-9]*\\.?[0-9]+";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO restrict to numeric and decimal point

        //Set kcalInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        kcalInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    kcalInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set proteinInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        proteinInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    proteinInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set fatInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        fatInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fatInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set carbsInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        carbsInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    carbsInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set sugarInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        sugarInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    sugarInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set fibreInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        fibreInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fibreInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Set cholesterolInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        fatInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    cholesterolInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML public void submitButtonAction() throws SQLException {
        DatabaseHandler dbh = DatabaseHandler.getInstance();

        if (checkInputs()){
            Stage parentScene = (Stage) submitButton.getScene().getWindow();
            dbh.addNutritionItem(nameInput.getText(),
                                Double.parseDouble(kcalInput.getText()),
                                Double.parseDouble(proteinInput.getText()),
                                Double.parseDouble(fatInput.getText()),
                                Double.parseDouble(carbsInput.getText()),
                                Double.parseDouble(sugarInput.getText()),
                                Double.parseDouble(fibreInput.getText()),
                                Double.parseDouble(cholesterolInput.getText()));

            System.out.println("Item Created: " + nameInput.getText());

            parentScene.close();

        }

    }

    public boolean checkInputs(){
        return checkFirstName() && checkKcalInput() && checkProteinInput() && checkFatInput() && checkCarbsInput() &&
               checkSugarInput() && checkFibreInput() && checkCholesterolInput();
    }

    /**
     * Method to check name field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFirstName(){
        String name = nameInput.getText();
        if (name.matches(NAMEINPUTREGEX)){
            nameInputPopUp.setText("");
            return true;
        }
        else{
            nameInputPopUp.setText("Please enter a valid Name");
            return false;
        }
    }

    /**
     * Method to check kcal field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkKcalInput(){
        String kcal = kcalInput.getText();
        if (kcal.matches(DOUBLEINPUTREGEX)){
            kcalInputPopUp.setText("");
            return true;
        }
        else{
            kcalInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check protein field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkProteinInput(){
        String protein = proteinInput.getText();
        if (protein.matches(DOUBLEINPUTREGEX)){
            proteinInputPopUp.setText("");
            return true;
        }
        else{
            proteinInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check fat field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFatInput(){
        String fat = fatInput.getText();
        if (fat.matches(DOUBLEINPUTREGEX)){
            fatInputPopUp.setText("");
            return true;
        }
        else{
            fatInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check carbs field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkCarbsInput(){
        String carbs = carbsInput.getText();
        if (carbs.matches(DOUBLEINPUTREGEX)){
            carbsInputPopUp.setText("");
            return true;
        }
        else{
            carbsInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check sugar field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkSugarInput(){
        String sugar = sugarInput.getText();
        if (sugar.matches(DOUBLEINPUTREGEX)){
            sugarInputPopUp.setText("");
            return true;
        }
        else{
            sugarInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check fibre field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFibreInput(){
        String fibre = fibreInput.getText();
        if (fibre.matches(DOUBLEINPUTREGEX)){
            fibreInputPopUp.setText("");
            return true;
        }
        else{
            fibreInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check cholesterol field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkCholesterolInput(){
        String cholesterol = cholesterolInput.getText();
        if (cholesterol.matches(DOUBLEINPUTREGEX)){
            cholesterolInputPopUp.setText("");
            return true;
        }
        else{
            cholesterolInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

}
