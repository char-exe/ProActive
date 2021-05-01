package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.NutritionItem;

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
public class CreateAdvancedNutritionItemController implements Initializable {
    @FXML public TextField nameInput;
    @FXML public TextField kcalInput;
    @FXML public TextField proteinInput;
    @FXML public TextField fatInput;
    @FXML public TextField carbsInput;
    @FXML public TextField sugarInput;
    @FXML public TextField fibreInput;
    @FXML public TextField cholesterolInput;
    @FXML public TextField sodiumInput;
    @FXML public TextField potassiumInput;
    @FXML public TextField calciumInput;
    @FXML public TextField magnesiumInput;
    @FXML public TextField phosphorousInput;
    @FXML public TextField ironInput;
    @FXML public TextField copperInput;
    @FXML public TextField zincInput;
    @FXML public TextField chlorideInput;
    @FXML public TextField seleniumInput;
    @FXML public TextField iodineInput;
    @FXML public TextField vitAInput;
    @FXML public TextField vitDInput;
    @FXML public TextField thiaminInput;
    @FXML public TextField riboflavinInput;
    @FXML public TextField niacinInput;
    @FXML public TextField vitB6Input;
    @FXML public TextField vitB12Input;
    @FXML public TextField folateInput;
    @FXML public TextField vitCInput;
    @FXML public Button submitButton;

    @FXML public Label nameInputPopUp;
    @FXML public Label kcalInputPopUp;
    @FXML public Label proteinInputPopUp;
    @FXML public Label fatInputPopUp;
    @FXML public Label carbsInputPopUp;
    @FXML public Label sugarInputPopUp;
    @FXML public Label fibreInputPopUp;
    @FXML public Label cholesterolInputPopUp;
    @FXML public Label sodiumInputPopUp;
    @FXML public Label potassiumInputPopUp;
    @FXML public Label calciumInputPopUp;
    @FXML public Label magnesiumInputPopUp;
    @FXML public Label phosphorousInputPopUp;
    @FXML public Label ironInputPopUp;
    @FXML public Label copperInputPopUp;
    @FXML public Label zincInputPopUp;
    @FXML public Label chlorideInputPopUp;
    @FXML public Label seleniumInputPopUp;
    @FXML public Label iodineInputPopUp;
    @FXML public Label vitAInputPopUp;
    @FXML public Label vitDInputPopUp;
    @FXML public Label thiaminInputPopUp;
    @FXML public Label riboflavinInputPopUp;
    @FXML public Label niacinInputPopUp;
    @FXML public Label vitB6InputPopUp;
    @FXML public Label vitB12InputPopUp;
    @FXML public Label folateInputPopUp;
    @FXML public Label vitCInputPopUp;

    private NutritionItem n;
    

    //Regex for nameInput textField
    private final String NAMEINPUTREGEX = "[a-zA-Z]*";

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

        //Set kcalInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        kcalInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    kcalInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set proteinInput to digits only
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        proteinInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    proteinInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set fatInput to digits only
        fatInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    fatInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set carbsInput to digits only
        carbsInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    carbsInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set sugarInput to digits only
        sugarInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    sugarInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set fibreInput to digits only
        fibreInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    fibreInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set cholesterolInput to digits only
        cholesterolInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    cholesterolInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set sodiumInput to digits only
        sodiumInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    sodiumInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set potassiumInput to digits only
        potassiumInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    potassiumInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set calciumInput to digits only
        calciumInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    calciumInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set magnesiumInput to digits only
        magnesiumInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    magnesiumInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set phosphorousInput to digits only
        phosphorousInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    phosphorousInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set ironInput to digits only
        ironInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    ironInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set copperInput to digits only
        copperInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    copperInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set zincInput to digits only
        zincInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    zincInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set chlorideInput to digits only
        chlorideInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    chlorideInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set seleniumInput to digits only
        seleniumInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    seleniumInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set iodineInput to digits only
        iodineInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    iodineInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set vitAInput to digits only
        vitAInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    vitAInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set vitDInput to digits only
        vitDInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    vitDInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set thiaminInput to digits only
        thiaminInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    thiaminInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set riboflavinInput to digits only
        riboflavinInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    riboflavinInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set niacinInput to digits only
        niacinInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    niacinInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set vitB6Input to digits only
        vitB6Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    vitB6Input.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set vitB12Input to digits only
        vitB12Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    vitB12Input.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set folateInput to digits only
        folateInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    folateInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Set vitCInput to digits only
        vitCInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^\\d*\\.?\\d*")) {
                    vitCInput.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });
    }

    /**
     * Method to act as a pseudo constructor for
     *
     * @param n Nutrition Item object to be passed in
     */
    public void initData(NutritionItem n){
        this.n = n;
    }

    /**
     * Method to handle the submit button event
     *
     * @throws SQLException Throws an SQLException whenever it is possible for an external force to affect SQL
     *                      execution
     */
    @FXML public void submitButtonAction() throws SQLException {
        DatabaseHandler dbh = DatabaseHandler.getInstance();

        //if (checkInputs()){

            System.out.println(n);
            /*
            Stage parentScene = (Stage) submitButton.getScene().getWindow();

            //TODO determine new inputs for nutrition items, once determined, create getters and setters in
            // NutritionItem class then set up on this controller

            dbh.addNutritionItem(n);

            System.out.println("Item Created: " + nameInput.getText());

            parentScene.close();

             */

       // }

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
    public boolean checkValidDouble(String str, String valToFind){
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
        if (kcal.matches(DOUBLEINPUTREGEX) && checkValidDouble(kcal, ".")){
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
        if (protein.matches(DOUBLEINPUTREGEX) && checkValidDouble(protein, ".")){
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
        if (fat.matches(DOUBLEINPUTREGEX) && checkValidDouble(fat, ".")){
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
        if (carbs.matches(DOUBLEINPUTREGEX) && checkValidDouble(carbs, ".")){
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
        if (sugar.matches(DOUBLEINPUTREGEX) && checkValidDouble(sugar, ".")){
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
        if (fibre.matches(DOUBLEINPUTREGEX) && checkValidDouble(fibre, ".")){
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
        if (cholesterol.matches(DOUBLEINPUTREGEX) && checkValidDouble(cholesterol, ".")){
            cholesterolInputPopUp.setText("");
            return true;
        }
        else{
            cholesterolInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }
}
