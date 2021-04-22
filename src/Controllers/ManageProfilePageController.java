package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import sample.User;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageProfilePageController implements Initializable {
    private User user;

    @FXML ChoiceBox sexCombo;
    @FXML DatePicker datePick;
    @FXML TextArea heightField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(User user){
        this.user = user;
    }
}
