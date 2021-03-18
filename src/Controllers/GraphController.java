package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements Initializable
{

    public GraphController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Graph.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading Graph.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Graph.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading Graph.fxml");
        }
    }

}
