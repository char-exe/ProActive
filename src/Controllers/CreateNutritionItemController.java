package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.NutritionItem;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Class to allow for the creation of custom nutrition items
 *
 * @author Owen Tasker
 *
 * @version 1.0
 */
public class CreateNutritionItemController implements Initializable {
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

    @FXML public Button advancedCreationButton;
    @FXML public Button cancelButton;


    //https://www.regular-expressions.info/floatingpoint.html
    //Regex for inputfields that will take doubles
    private final String DOUBLEINPUTREGEX = "[-+]?[0-9]*\\.?[0-9]+";

    /**
     * Method to be ran after all FXML elements have been loaded, used for imposing restrictions on these FXML
     * elements
     *
     * @param url FXML defined parameter
     * @param resourceBundle FXML defined parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO Update regex to match protein, add error checking for multiple decimal points in the checks methods

        //Set kcalInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        kcalInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*")) {
                kcalInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
            }
        });

        //Set proteinInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        proteinInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*")) {
                proteinInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
            }
        });

        //Set fatInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        fatInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*")) {
                fatInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
            }
        });

        //Set carbsInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        carbsInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*")) {
                carbsInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
            }
        });

        //Set sugarInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        sugarInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*")) {
                sugarInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
            }
        });

        //Set fibreInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        fibreInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*")) {
                fibreInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
            }
        });

        //Set cholesterolInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        fatInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*\\.?\\d*")) {
                cholesterolInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
            }
        });
    }

    /**
     * Method to handle the submit button event
     *
     * @throws SQLException Throws an SQLException whenever it is possible for an external force to affect SQL
     *                      execution
     */
    @FXML public void submitButtonAction() throws SQLException {
        DatabaseHandler dbh = DatabaseHandler.getInstance();

        if (checkInputs()){
            Stage parentScene = (Stage) submitButton.getScene().getWindow();

            NutritionItem n = new NutritionItem(nameInput.getText(),
                    Double.parseDouble(kcalInput.getText()),
                    Double.parseDouble(proteinInput.getText()),
                    Double.parseDouble(fatInput.getText()),
                    Double.parseDouble(carbsInput.getText()),
                    Double.parseDouble(sugarInput.getText()),
                    Double.parseDouble(fibreInput.getText()),
                    Double.parseDouble(cholesterolInput.getText()));

            dbh.addNutritionItem(n);

            System.out.println("Item Created: " + nameInput.getText());

            parentScene.close();
        }
    }

    @FXML public void cancelButtonAction() {
        Stage parentScene = (Stage) cancelButton.getScene().getWindow();

        parentScene.close();
    }

    /**
     * Method to check that a string representing a double is a valid double before casting, copied and adapted from
     * https://stackoverflow.com/questions/767759/occurrences-of-substring-in-a-string
     *
     * @param str String that we are checking
     * @param valToFind Value that we are checking for
     *
     * @return Returns true if value is a valid double value (has less than 2 decimal points)
     *
     */
    public boolean checkValidDouble(String str, String valToFind) {
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){
            lastIndex = str.indexOf(valToFind, lastIndex);

            if (lastIndex != -1){
                count++;
                lastIndex+=valToFind.length();
            }
        }
        return count <= 1;
    }

    /**
     * Method to clean up regex checks
     *
     * @return Returns true if all checks are passed, false otherwise
     */
    public boolean checkInputs() {
        return checkFirstName() && checkKcalInput() && checkProteinInput() && checkFatInput() && checkCarbsInput() &&
               checkSugarInput() && checkFibreInput() && checkCholesterolInput();
    }

    /**
     * Method to check name field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFirstName() {
        String name = nameInput.getText();
        //Regex for nameInput textField
        if (name != null) {
            nameInputPopUp.setText("");
            return true;
        }
        else {
            nameInputPopUp.setText("Please enter a valid Name");
            return false;
        }
    }

    /**
     * Method to check kcal field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkKcalInput() {
        String kcal = kcalInput.getText();
        if (kcal.matches(DOUBLEINPUTREGEX) && checkValidDouble(kcal, ".")) {
            kcalInputPopUp.setText("");
            return true;
        }
        else {
            kcalInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check protein field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkProteinInput() {
        String protein = proteinInput.getText();
        if (protein.matches(DOUBLEINPUTREGEX) && checkValidDouble(protein, ".")) {
            proteinInputPopUp.setText("");
            return true;
        }
        else {
            proteinInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check fat field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFatInput() {
        String fat = fatInput.getText();
        if (fat.matches(DOUBLEINPUTREGEX) && checkValidDouble(fat, ".")) {
            fatInputPopUp.setText("");
            return true;
        }
        else {
            fatInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check carbs field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkCarbsInput() {
        String carbs = carbsInput.getText();
        if (carbs.matches(DOUBLEINPUTREGEX) && checkValidDouble(carbs, ".")) {
            carbsInputPopUp.setText("");
            return true;
        }
        else {
            carbsInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check sugar field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkSugarInput() {
        String sugar = sugarInput.getText();
        if (sugar.matches(DOUBLEINPUTREGEX) && checkValidDouble(sugar, ".")) {
            sugarInputPopUp.setText("");
            return true;
        }
        else {
            sugarInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check fibre field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFibreInput() {
        String fibre = fibreInput.getText();
        if (fibre.matches(DOUBLEINPUTREGEX) && checkValidDouble(fibre, ".")) {
            fibreInputPopUp.setText("");
            return true;
        }
        else {
            fibreInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check cholesterol field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkCholesterolInput() {
        String cholesterol = cholesterolInput.getText();
        if (cholesterol.matches(DOUBLEINPUTREGEX) && checkValidDouble(cholesterol, ".")) {
            cholesterolInputPopUp.setText("");
            return true;
        }
        else {
            cholesterolInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Private method controlling the advanced creation button action.
     *
     * @throws IOException if the loading of the advanced item page throws an IOException.
     */
    @FXML private void advancedCreationButtonAction() throws IOException {
        Stage parentScene = (Stage) advancedCreationButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/CreateAdvancedNutritionItem.fxml"));

        Parent advancedParent = loader.load();

        Scene sceneEmail = new Scene(advancedParent);

        stage.setScene(sceneEmail);

        CreateAdvancedNutritionItemController controller = loader.getController();
        stage.setScene(sceneEmail);

        parentScene.close();
        stage.show();
    }

}
