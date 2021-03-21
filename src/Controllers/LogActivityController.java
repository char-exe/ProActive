package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class LogActivityController implements Initializable {

    @FXML private TableView breakfastTable;
    @FXML private TableView lunchTable;
    @FXML private TableView dinnerTable;
    @FXML private TableView snackTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        breakfastTable.setPlaceholder(new Label("Add food item or custom item"));
        lunchTable.setPlaceholder(new Label("Add food item or custom item"));
        dinnerTable.setPlaceholder(new Label("Add food item or custom item"));
        snackTable.setPlaceholder(new Label("Add food item or custom item"));

    }
}
