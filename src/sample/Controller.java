package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;


public class Controller {

    public Controller() {

    }

    @FXML private BorderPane main;

    @FXML private void loginScreen() throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        main.setCenter(vBox);
    }

}
