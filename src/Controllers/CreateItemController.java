package Controllers;

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

    }

    @FXML public void submitButtonAction(){
        System.out.println(nameInput.getText());
    }
}
