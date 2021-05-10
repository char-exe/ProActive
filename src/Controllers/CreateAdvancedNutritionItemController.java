package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
    @FXML public Button submitButton, cancelButton;

    @FXML public TextField  nameInput, kcalInput, proteinInput, fatInput, carbsInput, sugarInput, fibreInput,
                            cholesterolInput, sodiumInput, potassiumInput, calciumInput, magnesiumInput,
                            phosphorousInput, ironInput, copperInput, zincInput, chlorideInput, seleniumInput,
                            iodineInput, vitAInput, vitDInput, thiaminInput, riboflavinInput, niacinInput, vitB6Input,
                            vitB12Input, folateInput, vitCInput;

    @FXML public Label      nameInputPopUp, kcalInputPopUp, proteinInputPopUp, fatInputPopUp, carbsInputPopUp,
                            sugarInputPopUp, fibreInputPopUp, cholesterolInputPopUp, sodiumInputPopUp,
                            potassiumInputPopUp,calciumInputPopUp, magnesiumInputPopUp, phosphorousInputPopUp,
                            ironInputPopUp, copperInputPopUp, zincInputPopUp, chlorideInputPopUp, seleniumInputPopUp,
                            iodineInputPopUp, vitAInputPopUp, vitDInputPopUp, thiaminInputPopUp, riboflavinInputPopUp,
                            niacinInputPopUp, vitB6InputPopUp, vitB12InputPopUp, folateInputPopUp, vitCInputPopUp;

    private NutritionItem n;
    

    //Regex for nameInput textField
    private final String NAMEINPUTREGEX = "[a-zA-Z]*";

    //https://www.regular-expressions.info/floatingpoint.html
    //Regex for inputfields that will take doubles
    private final String DOUBLEINPUTREGEX = "[-+]?[0-9]*\\.?[0-9]+";

    //Regex for inputField checking
    private final String INPUTFIELDNONNUMERIC = "^\\d*\\.?\\d*";

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
        //Taken and adapted from below
        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        kcalInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                kcalInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set proteinInput to digits only
        proteinInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                proteinInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set fatInput to digits only
        fatInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                fatInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set carbsInput to digits only
        carbsInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                carbsInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set sugarInput to digits only
        sugarInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                sugarInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set fibreInput to digits only
        fibreInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                fibreInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set cholesterolInput to digits only
        cholesterolInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                cholesterolInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set sodiumInput to digits only
        sodiumInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                sodiumInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set potassiumInput to digits only
        potassiumInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                potassiumInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set calciumInput to digits only
        calciumInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                calciumInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set magnesiumInput to digits only
        magnesiumInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                magnesiumInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set phosphorousInput to digits only
        phosphorousInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                phosphorousInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set ironInput to digits only
        ironInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                ironInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set copperInput to digits only
        copperInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                copperInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set zincInput to digits only
        zincInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                zincInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set chlorideInput to digits only
        chlorideInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                chlorideInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set seleniumInput to digits only
        seleniumInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                seleniumInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set iodineInput to digits only
        iodineInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                iodineInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set vitAInput to digits only
        vitAInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                vitAInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set vitDInput to digits only
        vitDInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                vitDInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set thiaminInput to digits only
        thiaminInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                thiaminInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set riboflavinInput to digits only
        riboflavinInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                riboflavinInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set niacinInput to digits only
        niacinInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                niacinInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set vitB6Input to digits only
        vitB6Input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                vitB6Input.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set vitB12Input to digits only
        vitB12Input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                vitB12Input.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set folateInput to digits only
        folateInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                folateInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });

        //Set vitCInput to digits only
        vitCInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(INPUTFIELDNONNUMERIC)) {
                vitCInput.setText(newValue.replaceAll(INPUTFIELDNONNUMERIC, ""));
            }
        });
    }

    /**
     * Method to act as a pseudo constructor for advancedNutritionItemController

     */
    public void initData(){

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

            n = new NutritionItem(
                    nameInput.getText(),
                    Double.parseDouble(kcalInput.getText()),
                    Double.parseDouble(proteinInput.getText()),
                    Double.parseDouble(fatInput.getText()),
                    Double.parseDouble(carbsInput.getText()),
                    Double.parseDouble(sugarInput.getText()),
                    Double.parseDouble(fibreInput.getText()),
                    Double.parseDouble(cholesterolInput.getText()),
                    Double.parseDouble(sodiumInput.getText()),
                    Double.parseDouble(potassiumInput.getText()),
                    Double.parseDouble(calciumInput.getText()),
                    Double.parseDouble(magnesiumInput.getText()),
                    Double.parseDouble(phosphorousInput.getText()),
                    Double.parseDouble(ironInput.getText()),
                    Double.parseDouble(copperInput.getText()),
                    Double.parseDouble(zincInput.getText()),
                    Double.parseDouble(chlorideInput.getText()),
                    Double.parseDouble(seleniumInput.getText()),
                    Double.parseDouble(iodineInput.getText()),
                    Double.parseDouble(vitAInput.getText()),
                    Double.parseDouble(vitDInput.getText()),
                    Double.parseDouble(thiaminInput.getText()),
                    Double.parseDouble(riboflavinInput.getText()),
                    Double.parseDouble(niacinInput.getText()),
                    Double.parseDouble(vitB6Input.getText()),
                    Double.parseDouble(vitB12Input.getText()),
                    Double.parseDouble(folateInput.getText()),
                    Double.parseDouble(vitCInput.getText())
            );

            System.out.println(n);
            Stage parentScene = (Stage) submitButton.getScene().getWindow();

            dbh.addNutritionItem(n);

            System.out.println("Item Created: " + nameInput.getText());

            parentScene.close();


        }

    }

    /**
     * Method to cancel creating an advanced nutrition item and close the window
     */
    @FXML public void cancelButtonAction(){
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
               checkSugarInput() && checkFibreInput() && checkCholesterolInput() && checkSodiumInput() &&
               checkPotassiumInput() && checkCalciumInput() && checkMagnesiumInput() && checkPhosphorousInput() &&
               checkIronInput() && checkCopperInput() && checkZincInput() && checkChlorideInput() &&
               checkSeleniumInput() && checkIodineInput() && checkVitAInput() && checkVitDInput() &&
               checkThiaminInput() && checkRiboflavinInput() && checkNiacinInput() && checkVitB6Input() &&
               checkVitB12Input() && checkFolateInput() && checkVitCInput();
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

    /**
     * Method to check sodium field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkSodiumInput(){
        String sodium = sodiumInput.getText();
        if (sodium.matches(DOUBLEINPUTREGEX) && checkValidDouble(sodium, ".")){
            sodiumInputPopUp.setText("");
            return true;
        }
        else{
            sodiumInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check potassium field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkPotassiumInput(){
        String potassium = potassiumInput.getText();
        if (potassium.matches(DOUBLEINPUTREGEX) && checkValidDouble(potassium, ".")){
            potassiumInputPopUp.setText("");
            return true;
        }
        else{
            potassiumInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check calcium field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkCalciumInput(){
        String calcium = calciumInput.getText();
        if (calcium.matches(DOUBLEINPUTREGEX) && checkValidDouble(calcium, ".")){
            calciumInputPopUp.setText("");
            return true;
        }
        else{
            calciumInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check magnesium field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkMagnesiumInput(){
        String magnesium = magnesiumInput.getText();
        if (magnesium.matches(DOUBLEINPUTREGEX) && checkValidDouble(magnesium, ".")){
            magnesiumInputPopUp.setText("");
            return true;
        }
        else{
            magnesiumInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check phosphorous field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkPhosphorousInput(){
        String phospherous = phosphorousInput.getText();
        if (phospherous.matches(DOUBLEINPUTREGEX) && checkValidDouble(phospherous, ".")){
            phosphorousInputPopUp.setText("");
            return true;
        }
        else{
            phosphorousInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check iron field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkIronInput(){
        String iron = ironInput.getText();
        if (iron.matches(DOUBLEINPUTREGEX) && checkValidDouble(iron, ".")){
            ironInputPopUp.setText("");
            return true;
        }
        else{
            ironInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check copper field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkCopperInput(){
        String copper = copperInput.getText();
        if (copper.matches(DOUBLEINPUTREGEX) && checkValidDouble(copper, ".")){
            copperInputPopUp.setText("");
            return true;
        }
        else{
            copperInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check zinc field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkZincInput(){
        String zinc = zincInput.getText();
        if (zinc.matches(DOUBLEINPUTREGEX) && checkValidDouble(zinc, ".")){
            zincInputPopUp.setText("");
            return true;
        }
        else{
            zincInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check chloride field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkChlorideInput(){
        String chloride = chlorideInput.getText();
        if (chloride.matches(DOUBLEINPUTREGEX) && checkValidDouble(chloride, ".")){
            chlorideInputPopUp.setText("");
            return true;
        }
        else{
            chlorideInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check selenium field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkSeleniumInput(){
        String selenium = seleniumInput.getText();
        if (selenium.matches(DOUBLEINPUTREGEX) && checkValidDouble(selenium, ".")){
            seleniumInputPopUp.setText("");
            return true;
        }
        else{
            seleniumInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check iodine field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkIodineInput(){
        String iodine = iodineInput.getText();
        if (iodine.matches(DOUBLEINPUTREGEX) && checkValidDouble(iodine, ".")){
            iodineInputPopUp.setText("");
            return true;
        }
        else{
            iodineInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check vitA field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkVitAInput(){
        String vitA = vitAInput.getText();
        if (vitA.matches(DOUBLEINPUTREGEX) && checkValidDouble(vitA, ".")){
            vitAInputPopUp.setText("");
            return true;
        }
        else{
            vitAInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check vitD field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkVitDInput(){
        String vitD = vitDInput.getText();
        if (vitD.matches(DOUBLEINPUTREGEX) && checkValidDouble(vitD, ".")){
            vitDInputPopUp.setText("");
            return true;
        }
        else{
            vitDInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check thiamin field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkThiaminInput(){
        String thiamin = thiaminInput.getText();
        if (thiamin.matches(DOUBLEINPUTREGEX) && checkValidDouble(thiamin, ".")){
            thiaminInputPopUp.setText("");
            return true;
        }
        else{
            thiaminInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check riboflavin field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkRiboflavinInput(){
        String riboflavin = riboflavinInput.getText();
        if (riboflavin.matches(DOUBLEINPUTREGEX) && checkValidDouble(riboflavin, ".")){
            riboflavinInputPopUp.setText("");
            return true;
        }
        else{
            riboflavinInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check niacin field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkNiacinInput(){
        String niacin = niacinInput.getText();
        if (niacin.matches(DOUBLEINPUTREGEX) && checkValidDouble(niacin, ".")){
            niacinInputPopUp.setText("");
            return true;
        }
        else{
            niacinInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check vitB6 field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkVitB6Input(){
        String vitB6 = vitB6Input.getText();
        if (vitB6.matches(DOUBLEINPUTREGEX) && checkValidDouble(vitB6, ".")){
            vitB6InputPopUp.setText("");
            return true;
        }
        else{
            vitB6InputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check vitB12 field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkVitB12Input(){
        String vitB12 = vitB12Input.getText();
        if (vitB12.matches(DOUBLEINPUTREGEX) && checkValidDouble(vitB12, ".")){
            vitB12InputPopUp.setText("");
            return true;
        }
        else{
            vitB12InputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check folate field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkFolateInput(){
        String folate = folateInput.getText();
        if (folate.matches(DOUBLEINPUTREGEX) && checkValidDouble(folate, ".")){
            folateInputPopUp.setText("");
            return true;
        }
        else{
            folateInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

    /**
     * Method to check vitC field matches in regex
     *
     * @return True if check is passed
     */
    @FXML protected boolean checkVitCInput(){
        String vitC = vitCInput.getText();
        if (vitC.matches(DOUBLEINPUTREGEX) && checkValidDouble(vitC, ".")){
            vitCInputPopUp.setText("");
            return true;
        }
        else{
            vitCInputPopUp.setText("Please enter a valid value");
            return false;
        }
    }

}
