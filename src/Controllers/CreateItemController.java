package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
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

    @FXML public void submitButtonAction(){
        System.out.println(nameInput.getText());
    }
}
